import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    scenarios: {
        debug_test: {
            executor: 'constant-arrival-rate',
            rate: 1,
            timeUnit: '1s',
            duration: '10s',
            preAllocatedVUs: 2,
            maxVUs: 5,
        },
    },
};

// customerId 범위를 1001~11000으로 설정
function getRandomCustomerId() {
    return (Math.floor(Math.random() * 10000) + 1001).toString();
}

// fruitId 범위를 1~1000으로 설정
function getRandomFruitId() {
    return (Math.floor(Math.random() * 1000) + 1).toString();
}

// 100단위로 끝나는 과일 가격 생성 (1000 ~ 10000원 사이)
function getRandomFruitPrice() {
    return (Math.floor(Math.random() * 100) + 10) * 100;
}

// 과일 주문 목록 생성 (최소 1개, 최대 5개)
function getRandomOrderFruits() {
    let numFruits = Math.floor(Math.random() * 5) + 1; // 1~5개
    let orderFruits = [];

    for (let i = 0; i < numFruits; i++) {
        orderFruits.push({
            fruitId: getRandomFruitId(),
            fruitPrice: getRandomFruitPrice(),
            quantity: Math.floor(Math.random() * 5) + 1, // 최소 1개 ~ 최대 5개
        });
    }
    return orderFruits;
}

export default function () {
    let payload = JSON.stringify({
        customerId: getRandomCustomerId(),
        orderFruits: getRandomOrderFruits(),
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    console.log(`주문 요청: ${payload}`);

    let res = http.post('http://host.docker.internal:8080/api/orders', payload, params);

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
        'response contains success message': () => jsonResponse.message.includes("주문 성공"),
    });

    if (!isSuccess) {
        console.error(`요청 실패: 상태 코드=${res.status}, 응답=${JSON.stringify(jsonResponse)}`);
    }

    sleep(1);
}
