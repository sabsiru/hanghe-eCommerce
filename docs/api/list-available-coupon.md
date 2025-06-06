# API 명세 - 발급 가능한 쿠폰 조회 API

## 1. 개요

- **발급 가능한 쿠폰 조회 API**는 시스템에 등록된 쿠폰 중 현재 **발급 가능한 쿠폰 목록**을 제공합니다.
- 사용자는 이 목록을 보고 원하는 쿠폰을 발급받을 수 있습니다.

---

## 2. API 정보

| 항목         | 내용                                        |
|------------|-------------------------------------------|
| **API 명칭** | 발급 가능한 쿠폰 조회 API                          |
| **설명**     | 시스템에 등록된 쿠폰 중 **발급 가능 상태인 쿠폰 목록을 반환**합니다. |
| **관련 도메인** | Coupon                                    |

---

## 3. 요청

- **Method**: `GET`
- **Endpoint**: `/coupon/available`

#### 3.1 Request Body

- 없음

---

## 4. 응답

#### 4.1 성공 시

```
{
"data": [
{
"couponId": 101,
"name": "10% 할인 쿠폰",
"discountRate": 10,
"expiredAt": "2025-04-30"
},
{
"couponId": 102,
"name": "5% 할인 쿠폰",
"discountRate": 5,
"expiredAt": "2025-04-10"
}
]
}
```

| 필드명          | 타입      | 설명                  |
|--------------|---------|---------------------|
| couponId     | Long    | 쿠폰 고유 식별자           |
| name         | String  | 쿠폰 이름               |
| discountRate | Integer | 할인율 (단위: %)         |
| expiresAt    | String  | 쿠폰 만료일 (yyyy-MM-dd) |
| message      | String  | 처리 결과 메시지           |

#### 4.2 상태 코드

| 코드  | 설명       |
|-----|----------|
| 200 | 조회 성공    |
| 500 | 내부 서버 오류 |

---

## 5. 예외 처리

| 예외 상황  | HTTP 상태 | 설명                       |
|--------|---------|--------------------------|
| 시스템 오류 | 500     | DB 연결 실패 등 서버 내부 오류 발생 시 |

---

## 6. 테스트 포인트

| 테스트 항목      | 검증 내용                   |
|-------------|-------------------------|
| 쿠폰 목록 반환 검증 | 발급 가능한 쿠폰 목록이 정확히 반환되는가 |
| 필드 검증       | 응답에 포함된 쿠폰 정보 필드들이 정확한가 |

[돌아가기](../../README.md)