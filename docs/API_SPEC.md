##API명세서
- 1. 상품조회
- HTTP METHOD : GET
- END POINT : /products
- REQUEST : 
  - Query Parameter : 페이지번호(page) 및 페이지크기(size)
  - 기본값:page=0,size=9
- RESPONSE :
```json
{
    "productId": 1,
    "productName": "상품명1",
    "productPrice": 10000,
    "stockQuantity": 50,
    "isPublished": true
  }
```
- 2. 상품주문
- HTTP METHOD : POST
- END POINT : /orders
- REQUEST :
```json
{
  "products": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ],
  "customerId": 123
}
```
- RESPONSE :
```json
{
  "orderId": 1001,
  "customerId": 123,
  "orderDate": "2025-01-01T12:00:00",
  "orderSheets": [
    {
      "productId": 1,
      "productName": "상품명1",
      "quantity": 2,
      "productPrice": 10000,
      "subTotal": 20000
    },
    {
      "productId": 2,
      "productName": "상품명2",
      "quantity": 1,
      "productPrice": 15000,
      "subTotal": 15000
    }
  ],
  "totalAmount": 35000
}
```
3. 인기 판매 상품 조회
- HTTP METHOD : GET
- END POINT : /products/top-sold
- REQUEST :
    - Query Parameter : 최근 N일 간(day) 및 최대 반환 개수(limit)
    - 기본값:page=3,size=5
- RESPONSE : 
```json
[
  {
    "productId": 1,
    "productName": "상품명1",
    "totalQuantitySold": 70,
    "totalSalesAmount": 700000
  },
  {
    "productId": 2,
    "productName": "상품명2",
    "totalQuantitySold": 60,
    "totalSalesAmount": 600000
  },
  {
    "productId": 3,
    "productName": "상품명3",
    "totalQuantitySold": 50,
    "totalSalesAmount": 500000
  },
  {
    "productId": 4,
    "productName": "상품명4",
    "totalQuantitySold": 40,
    "totalSalesAmount": 400000
  },
  {
    "productId": 5,
    "productName": "상품명5",
    "totalQuantitySold": 30,
    "totalSalesAmount": 300000
  }
]
```
4. 잔액 조회
- HTTP METHOD : GET
- END POINT : /wallets/{customerId}
- REQUEST : customerId(PathVariable)
- RESPONSE :
```json
{
  "walletId": 1,
  "customerId": 123,
  "balance": 100000
}
```
5. 잔액 충전
- HTTP METHOD : POST
- END POINT : /wallets/{customerId}/charge
- REQUEST : 
```json
{
  "chargeAmount": 50000
}
```
- RESPONSE :
```json
{
  "walletId": 1,
  "customerId": 123,
  "balance": 150000
}
```
6. 쿠폰 발급
- HTTP METHOD : POST
- END POINT : /customers/{customerId}/coupons
- REQUEST : customerId(PathVariable)
- RESPONSE :
```json
{
  "couponId": 1,
  "customerId": 123,
  "status": "ISSUED",
  "issuedAt": "2025-01-01T10:00:00",
  "expiredAt": "2025-01-31T23:59:59"
}
```
7. 보유 쿠폰 목록 조회
- HTTP METHOD : GET
- END POINT : /customers/{customerId}/coupons
- REQUEST : customerId(PathVariable)
- RESPONSE(SUCCESS) :
```json
[
  {
    "couponId": 1,
    "name": "FIRST_COME",
    "discountAmount": 1000,
    "status": "ISSUED",
    "issuedAt": "2025-01-01T10:00:00",
    "expiredAt": "2025-01-31T23:59:59"
  },
  {
    "couponId": 2,
    "name": "WELCOME",
    "discountAmount": 2000,
    "status": "ISSUED",
    "issuedAt": "2025-01-01T10:00:00",
    "expiredAt": "2025-01-31T23:59:59"
  }
]
```
- RESPONSE(FAIL) : 400번 에러
```json
{
  "error": "ALREADY_ISSUED",
  "message": "이미 쿠폰이 발급되었습니다."
}
```
- RESPONSE(FAIL) : 409번 에러
```json
{
  "error": "OUT_OF_STOCK",
  "message": "잔여량이 부족합니다."
}
```

8. 주문한 상품을 결제
- HTTP METHOD : POST
- END POINT : /payments
- REQUEST :
```json
{
  "purchaseId": 1001,
  "walletId": 1,
  "couponId": 10
}
```
- RESPONSE :
```json
{
  "paymentId": 5001,
  "purchaseId": 1001,
  "amount": 30000,
  "paidAt": "2025-01-01T13:00:00",
  "status": "SUCCESS"
}
```
9. 데이터 분석을 위한 결제 성공시 실시간 전송
- HTTP METHOD : POST (내부적 호출)
- END POINT : /analytics/orders
- REQUEST :
```json
{
  "orderId": 1001,
  "customerId": 123,
  "orderDate": "2025-01-01T12:00:00",
  "totalAmount": 35000,
  "products": [
    {
      "productId": 1,
      "productName": "상품명1",
      "quantity": 2,
      "subTotal": 20000
    },
    {
      "productId": 2,
      "productName": "상품명2",
      "quantity": 1,
      "subTotal": 15000
    }
  ],
  "payment": {
    "paymentId": 5001,
    "amount": 30000,
    "paidAt": "2025-01-01T13:00:00",
    "status": "SUCCESS"
  }
}
```
- RESPONSE :
  - SUCCESS : 200 OK
  - FAIL : 500 Internal Server Error