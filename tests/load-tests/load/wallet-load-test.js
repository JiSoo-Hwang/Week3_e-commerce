import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
    scenarios: {
        // TPS를 점진적으로 증가시키면서 부하 테스트 진행
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

// 랜덤한 고객 ID 및 충전 금액 생성 함수
function getRandomCustomerId() {
    return (Math.floor(Math.random() * 10000) + 1001).toString();
}

function getRandomChargeAmount() {
    return Math.floor(Math.random() * 50 + 1) * 100;
}

export default function () {
    let payload = JSON.stringify({
        customerId: getRandomCustomerId(),
        chargeAmount: getRandomChargeAmount(),
    });

    let params = {
        headers: { 'Content-Type': 'application/json',
         },
    };

    let res = http.post('http://host.docker.internal:8080/api/wallets', payload, params);

    let jsonResponse = res.json();

    let isSuccess = check(jsonResponse, {
        'status is 200 OK': () => jsonResponse.status === "200 OK",
        'response contains balance': () => jsonResponse.content && jsonResponse.content.balance !== undefined,
    });

    sleep(1);
}
