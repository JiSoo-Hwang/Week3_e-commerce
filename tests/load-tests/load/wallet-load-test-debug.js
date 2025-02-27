import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    scenarios: {
        debug_test: {
            executor: 'constant-arrival-rate',
            rate: 1,  // 초당 1개 요청 (부하를 최소화)
            timeUnit: '1s',
            duration: '10s',  // 10초 동안 실행
            preAllocatedVUs: 2,  // 가상 사용자 2명
            maxVUs: 5,
        },
    },
};

// customerId 범위를 1001~11000으로 수정
function getRandomCustomerId() {
    return (Math.floor(Math.random() * 10000) + 1001).toString();
}

// 100단위로 끝나는 충전 금액 생성
function getRandomChargeAmount() {
    return (Math.floor(Math.random() * 50) + 1) * 100;
}

export default function () {
    let payload = JSON.stringify({
        customerId: getRandomCustomerId(),
        chargeAmount: getRandomChargeAmount(),
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    console.log(`요청: ${payload}`);

    let res = http.post('http://host.docker.internal:8080/api/wallets', payload, params);

    // 응답 확인 및 JSON 변환 예외 처리
    if (!res || !res.body) {
        console.error(`응답 없음: 상태 코드=${res.status}`);
        return;
    }

    let jsonResponse;
    try {
        jsonResponse = res.json();
    } catch (e) {
        console.error(`JSON 변환 실패: 상태 코드=${res.status}, 응답=${res.body}`);
        return;
    }

    let isSuccess = check(jsonResponse, {
        'status is 200 OK': () => jsonResponse.status === "200 OK",
        'response contains balance': () => jsonResponse.content && jsonResponse.content.balance !== undefined,
    });

    if (!isSuccess) {
        console.error(`요청 실패: 상태 코드=${res.status}, 응답=${JSON.stringify(jsonResponse)}`);
    }

    sleep(1);
}
