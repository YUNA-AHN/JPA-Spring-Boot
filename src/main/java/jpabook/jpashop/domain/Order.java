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
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id") // FK 이름이 member_id이 된다
    // 여기가 연관관계 주인이므로 여기 값 변경하면 반영됩니다!
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // java 8에서는 LocalDateTime 하면 자동으로 지원
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
}
