# 동시성 제어

## 1.[데이터베이스 락 개념 정리](docs/concurrency/db-lock.md)

## 2.[유저 포인트 동시성 제어 보고서](docs/concurrency/point-concurrency.md)

## 3.[쿠폰 발급 동시성 제어 보고서](docs/concurrency/coupon-concurrency.md)

## 4.[결제 시 재고 차감, 환불 시 재고 증가 동시성 제어 보고서](docs/concurrency/order-payment-product-concurrency.md)

# 캐시

## 1. [인기 상품 조회 개선 보고서](docs/redis/cache.md)

---
<details>
<summary>4주 차</summary>

##  조회 성능 개선 보고서

## 1. [인기상품조회](docs/performance/popular-product/popular-product.md)

## 2. [전체상품조회(페이징)](docs/performance/get-product-paging/get-product-paging.md)

</details>
---
<details>
<summary>1~3주 차</summary>


# 0. 마일스톤


- ### [마일스톤 바로가기](https://github.com/sabsiru/hanghe-eCommerce/milestones)

---

# 1. 요구사항 분석

- ### [요구사항 분석 바로가기](docs/Requirements.md)

---

# 2. ERD 설계

<details>
<summary>보기</summary>
    <img src="docs/diagram/erd.png">
</details>

---

# 3. 클래스 다이어그램 설계

<details>
<summary>보기</summary>
    <img src="docs/diagram/class_diagram.png">
</details>

---

# 4. 시퀀스 다이어그램

### 잔액 조회

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/get_point.png">
</details>

### 잔액 충전

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/charge_point.png">
</details>

### 상품 조회

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/list_product.png">
</details>

### 상품 상세보기

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/get_product.png">
</details>

### 상위 상품 조회

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/popular_products.png">
</details>

### 발급 가능한 쿠폰 조회

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/list-available-coupon.png">
</details>

### 쿠폰 발급

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/issue_coupon.png">
</details>

### 쿠폰 조회

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/list_coupon.png">
</details>

### 쿠폰 사용

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/use_coupon.png">
</details>

### 장바구니 추가

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/add_cart.png">
</details>

### 주문

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/order.png">
</details>

### 결제

<details>
<summary>보기</summary>
    <img src="docs/diagram/sequence/payment.png">
</details>

---

# 5. API 명세서

- ### [잔액 조회](docs/api/get-balance.md)
- ### [잔액 충전](docs/api/charge-point)
- ### [상품 조회](docs/api/list-products.md)
- ### [상품 상세보기](docs/api/get-product.md)
- ### [장바구니 추가](docs/api/add-cart.md)
- ### [상위 상품 조회](docs/api/popular-products.md)
- ### [쿠폰 발급](docs/api/issue-coupon.md)
- ### [쿠폰 사용](docs/api/use-coupon.md)
- ### [발급 가능 쿠폰 조회](docs/api/list-available-coupon.md)
- ### [사용자 쿠폰 조회](docs/api/list-coupon)
- ### [주문](docs/api/order.md)
- ### [결제](docs/api/payments.md)

---

# 6. Swagger UI

- ### [잔액 조회](docs/swagger/get-balance.md)
- ### [잔액 조회](docs/swagger/charge-balance.md)
- ### [상품 조회](docs/swagger/list-products.md)
- ### [상품 상세보기](docs/swagger/get-product.md)
- ### [상위 상품 조회](docs/swagger/popular-products.md)
- ### [발급 가능한 쿠폰 조회](docs/swagger/list-available-coupon.md)
- ### [쿠폰 발급](docs/swagger/issue-coupon.md)
- ### [사용자 쿠폰 조회](docs/swagger/list-coupon.md)
- ### [쿠폰 사용](docs/swagger/use-coupon.md)
- ### [주문](docs/swagger/order.md)
- ### [결제](docs/swagger/payments.md)

---

## 프로젝트

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
# hanghe-eCommerce
```

</details>