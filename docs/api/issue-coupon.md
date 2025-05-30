# API 명세 - 쿠폰 발급 API

## 1. 개요
- **쿠폰 발급 API**는 사용자가 **선착순 쿠폰**을 발급받을 수 있는 기능을 제공합니다.
- 발급된 쿠폰은 **사용자에게 부여**되며, 이후 **결제 시 쿠폰을 사용할 수 있습니다**.

---

## 2. API 정보

| 항목         | 내용                                                        |
|--------------|-------------------------------------------------------------|
| **API 명칭**  | 쿠폰 발급 API                                               |
| **설명**      | 사용자가 선착순으로 쿠폰을 발급받고, 발급된 쿠폰은 사용자가 **주문 시 사용할 수 있습니다**. |
| **관련 도메인**| User, Coupon, UserCoupon                       |

---

## 3. 요청

- **Method**: `POST`
- **Endpoint**: `/coupon/{userId}/issue`

#### 3.1 Path Variable

| 변수명 | 타입 | 필수 | 설명                                       |
|--------|------|------|--------------------------------------------|
| userId | Long | ✅   | 쿠폰을 발급받을 사용자 ID (고유 사용자 식별자) |

#### 3.2 Request Body
```
{
"couponId": 1
}
```
| 필드명  | 타입  | 필수 | 설명                                      |
|---------|-------|------|-------------------------------------------|
| couponId| Long  | ✅   | 발급할 쿠폰의 ID                           |

---

## 4. 응답

#### 4.1 성공 시
```
{
"message": "쿠폰 발급이 완료되었습니다."
}
```
| 필드명  | 타입  | 설명                                |
|---------|-------|-------------------------------------|
| message | String | 처리 결과 메시지                   |

#### 4.2 상태 코드

| 코드 | 설명        |
|------|-------------|
| 200  | 쿠폰 발급 성공   |
| 400  | 잘못된 요청      |
| 404  | 사용자 없음      |
| 409  | 쿠폰 발급 수량 초과 |

---

## 5. 예외 처리

| 예외 상황          | HTTP 상태 | 설명                               |
|---------------------|------------|-----------------------------------|
| 사용자 ID 누락 또는 오류 | 400        | URI 경로에 userId가 없거나 유효하지 않은 경우   |
| 사용자 없음         | 404        | 존재하지 않는 사용자 ID로 요청한 경우   |
| 쿠폰 수량 초과       | 409        | 발급할 수 있는 쿠폰 수량이 초과된 경우  |
| 형식 오류           | 400        | JSON body가 잘못되었거나 필드 누락   |

---

## 6. 테스트 포인트

| 테스트 항목         | 검증 내용                              |
|----------------------|---------------------------------------|
| 사용자 ID 식별 처리    | 요청에 포함된 userId가 정확히 식별되어 처리되는가  |
| 사용자 없음 처리       | 존재하지 않는 사용자 ID로 요청 시 404 응답이 반환되는가 |
| 쿠폰 발급 처리         | 유효한 요청 시 쿠폰이 발급되는가             |


[돌아가기](../../README.md)