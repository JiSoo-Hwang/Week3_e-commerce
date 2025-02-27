# 부하 테스트 계획서 - 상품 주문 기능

## 1. 개요
본 문서는 **상품 주문 기능**에 대한 부하 테스트 계획을 정리한 문서입니다.  
상품 주문 API는 여러 트랜잭션이 포함된 요청으로, 높은 부하 발생 시 성능 저하 가능성이 있어 이를 검증하고자 합니다.

---

## 2. 테스트 대상 선정 및 목적
### 2.1 테스트 대상
- **API 엔드포인트**: `/api/orders` (POST)
- **테스트 대상 선정 이유**:
    - 주문 요청은 주문 생성, 재고 차감, 주문 정보 입력, 주문 정보 저장 등의 복합적인 작업을 수행해야 함.
    - 다수의 사용자가 동시에 주문을 진행할 경우, 서버 부하가 크게 발생할 가능성이 높음.

### 2.2 테스트 목적
- 동시 주문 요청 시 **서버의 성능 및 안정성** 검증
- **최대 처리량(Throughput)과 응답 시간(Response Time)** 분석
- 서버 부하 증가 시 **에러 발생 여부 및 병목 현상** 확인
- 결과에 따른 **스케일링 전략 및 성능 개선 방향** 도출

---

## 3. 테스트 환경
| 항목 | 내용 |
|------|------|
| 테스트 도구 | k6 |
| 대상 API | `/api/orders` (POST) |
| 초당 요청 수 (TPS) | 100 ~ 1000 TPS |
| 테스트 지속 시간 | 9분 30초 |
| 기대 결과 | 95% 이상의 요청이 2초 이내 응답 |

---

## 4. 부하 테스트 시나리오
### 4.1 시나리오 개요
- **시뮬레이션 개요**: 실제 사용자가 몰리는 상황을 가정하여 부하 테스트 수행
- **시나리오 흐름**:
    1. 초당 **100건**의 주문 요청 발생
    2. 2분마다 증가하여 초당 **1000건(TPS)** 까지 도달
    3. 3분 동안 최대 부하 상태 유지
    4. 이후 1분 단위로 초당 500건 → 100건으로 점진적 감소
    5. 마지막 30초 동안 부하 제거

### 4.2 성공 기준
- **95% 이상의 요청이 2초 이내 응답**
- **오류율 1% 이하**
- **최대 TPS(1000)에서 서버가 안정적으로 동작하는지 확인**

---

## 5. 테스트 스크립트

<sub>※ 실제 테스트 실행 시, 설정 파일은 `configs/` 디렉터리에서 로드됩니다.</sub>
```javascript
import http from 'k6/http';
import { sleep, check } from 'k6';

// 부하 테스트 옵션 설정
export let options = {
scenarios: {
increasing_requests: {
executor: 'ramping-arrival-rate',
startRate: 100, // 시작 TPS: 100 (초당 100건)
timeUnit: '1s',
stages: [
{ duration: '2m', target: 500 },  // 2분 동안 초당 500건(TPS 500)으로 증가
{ duration: '2m', target: 1000 }, // 2분 동안 초당 1000건(TPS 1000)으로 증가
{ duration: '3m', target: 1000 }, // 3분 동안 TPS 1000 유지 (최대 부하 상태 유지)
{ duration: '1m', target: 500 },  // 1분 동안 초당 500건으로 감소
{ duration: '1m', target: 100 },  // 1분 동안 초당 100건으로 감소
{ duration: '30s', target: 0 },   // 30초 동안 점진적으로 부하 제거
],
preAllocatedVUs: 100,  // 미리 할당할 가상 사용자 수
maxVUs: 2000,          // 최대 가상 사용자 수 (최대 TPS 1000을 커버할 정도)
},
},
};

// 랜덤한 고객 ID 및 과일 주문 데이터 생성 함수
function getRandomCustomerId() {
return (Math.floor(Math.random() * 10000) + 1001).toString();
}

// 과일 주문 리스트 생성
function getRandomOrderFruits() {
let orderFruits = [];
let numFruits = Math.floor(Math.random() * 3) + 1; // 최소 1개 ~ 최대 3개 과일 주문
for (let i = 0; i < numFruits; i++) {
orderFruits.push({
fruitId: (Math.floor(Math.random() * 1000) + 1).toString(), // 1~1000 사이 과일 ID
fruitPrice: (Math.floor(Math.random() * 50) + 1) * 100,  // 100단위 가격 (예: 100, 200, 300 ... )
quantity: Math.floor(Math.random() * 5) + 1,  // 최소 1개 ~ 최대 5개 주문
});
}
return orderFruits;
}

// 부하 테스트 실행
export default function () {
let payload = JSON.stringify({
customerId: getRandomCustomerId(),
orderFruits: getRandomOrderFruits(),
});

    let params = {
        headers: { 'Content-Type': 'application/json' },
    };

    let res = http.post('http://host.docker.internal:8080/api/orders', payload, params);

    let jsonResponse;
    try {
        jsonResponse = res.json();
    } catch (e) {
        console.error(`JSON 변환 실패: 상태 코드=${res.status}, 응답=${res.body}`);
        return;
    }

    let isSuccess = check(jsonResponse, {
        'status is 200 OK': () => jsonResponse.status === "200 OK",
        'response contains success message': () => jsonResponse.message && jsonResponse.message.includes("주문 성공"),
    });

    if (!isSuccess) {
        console.error(`요청 실패: 상태 코드=${res.status}, 응답=${JSON.stringify(jsonResponse)}`);
    }

    sleep(1);
}
```
