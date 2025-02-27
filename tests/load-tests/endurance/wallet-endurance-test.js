import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
    scenarios: {
        // TPS를 점진적으로 증가시키면서 부하 테스트 진행
        endurance_test: {
            executor: 'constant-arrival-rate',
            rate: 500,
            timeUnit: '1s',
            duration: '1h',
            preAllocatedVUs: 500,
            maxVUs: 1000,
        },
    },
};

// 랜덤한 고객 ID 및 충전 금액 생성 함수
function getRandomCustomerId() {
    return Math.floor(Math.random() * 11000) + 1;
}

function getRandomChargeAmount() {
    let min = 1000;
    let max = 50000;
    return Math.floor(Math.random() * ((max - min) / 100 + 1)) * 100 + min;
}

export default function () {
    let payload = JSON.stringify({
        customerId: getRandomCustomerId().toString(),
        chargeAmount: getRandomChargeAmount(),
    });

    let params = {
        headers: { 'Content-Type': 'application/json' },
    };

    let res = http.post('http://localhost:8080/api/wallets', payload, params);

    check(res, {
        'status is 201': (r) => r.status === 201,
        'response contains success message': (r) => r.body.includes('금액 충전 완료'),
    });

    sleep(1);
}
