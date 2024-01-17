# 01. 주문, 주문 상품 엔티티 개발
트랜젝션 스크입트 패턴과 도메인 모델 패턴?

## 주문 엔티티 코드
- 생성 메서드 : createOrder()
  - 복잡한 생성은 별도의 생성 메서드가 있으면 좋다!
  - 오더가 연관관계를 걸면서 세팅, 상태랑 주문 시간 정보까지 모두 세팅하여 정리
  - 주문 엔티티 생성할 때 사용. 주문 회원, 배송정보, 주문상품의 정보를 받아 실제 주문 엔티티를 생성
- 비즈니스 로직 : 주문 취소 : cancel()
  - 주문 취소시 사용
  - 주문 상태를 취소로 변경하고 주문 상품에 주문 취소를 알린다.
  - 이미 배송 완료한 상품인 경우 주문 취소하지 못하도록 **예외 발생**
- 조회 로직 : 전체 주문 가격 조회
  - 주문 시 사용한 전체 주문 가격을 조회
  - 각각의 주문 상품 가격을 알아야 함
  - 연관된 주문 상품들의 가격을 조히해서 더한 값을 반환
```java
package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 이름이 member_id이 된다
    // 여기가 연관관계 주인이므로 여기 값 변경하면 반영됩니다!
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    // java 8에서는 LocalDateTime 하면 자동으로 지원
    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // 연관관계 메서드 //
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드 //
    // 복잡한 생성은 별도의 생성 메서드가 있으면 좋다!
    // 오더가 연관관계를 걸면서 세팅, 상태랑 주문 시간 정보까지 모두 세팅하여 정리
    // 주문 생성에 관련한 것은 여기만 고치면 된다
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        // 세팅
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        // for 문으로
        for (OrderItem orderItem : orderItems) {
            // order에 orderitem 넣어준다.
            order.addOrderItem(orderItem);
        }
        // Status를 ORDER로 강제, OrderDate는 현재 시간
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직 //
    /**주문 취소*/
    public void cancel() {
        // 체크 로직
        // 배송상태(DeliveryStatus)가 배송완료(COMP)라면 취소 불가
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // validation 통과하면 상태를 CANCEL로 변경
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            // 2개 주문할 수도 있으니까 각각 캔슬날려주는 것
            orderItem.cancel();
        }
    }

    // 조회 로직 : 계산이 필요한 경우 //
    /** 전체 주문 가격 조회 */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
        // 자바 스트림이나 람다 사용하면 깔끔하게 가능
        // alt+enter => collapse loop with strean
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
}

```

## 주문상품 엔티티 코드
- 생성 메서드 : createOrderItem() 
  - 주문 상품, 가격, 수량 정보를 사용해서 주문상품 엔티티를 생성
  - item.removeStock(count)를 호출해서 주문한 수량만큼 상품의 재고를 줄임
- 비즈니스 로직 : 주문 취소 : cancel()
  - getItem().addStock(count)를 호출해서 취소한 주문 수량만큼 상품의 재고 증가
- 전체 조회 : 주문 가격 조회 : getTotalPrice() : 주문 가격에 수량을 곱한 값을 반환한다.
```java
package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    // 생성 메서드 //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 넘어온 것 만큼 재고 감소
        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직 //
    public void cancel() {
        // 재고수량 원복
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}

```