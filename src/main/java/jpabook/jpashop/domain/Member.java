package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // mappedBy : "member" orders의 member 필드에 의해서 매핑된 것! 연관관계 주인이 아님을 표시
    // 읽기 전용, 여기 값을 변경한다고 반영되지 않는다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    // 객체는 변경포인트가 2군데, 테이블에서는 FK 하나만 변경하면 된다..! 둘 중 하나를 연관관계 주인
}
