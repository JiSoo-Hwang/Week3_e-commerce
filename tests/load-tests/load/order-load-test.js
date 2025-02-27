import http from 'k6/http';
import { sleep, check } from 'k6';
import path from 'path';
import { readFileSync } from 'fs';

// 부하 테스트 옵션 설정
const configPath = path.join(__dirname, '../configs/load-options.json');
export let options = JSON.parse(readFileSync(configPath));

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
            fruitPrice: (Math.floor(Math.random() * 50) + 1) * 100,  // 100단위 가격 (예: 100, 200, 300 ...)
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