package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // 기본인 ORDINARY로 넣는 경우 값 추가시 꼬일 수 있으므로 무조건 STRING!
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP

}
