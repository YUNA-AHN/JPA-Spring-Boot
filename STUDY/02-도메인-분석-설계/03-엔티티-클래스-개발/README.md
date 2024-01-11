# 03. 엔티티 클래스 개발
예제에서는 엔티티 클랫에 Getter, Setter를 모두 열고 단순히 설계 예정!
- 실무에서는 가급적 Getter는 열어두고, Setter는 꼭 필용한 경우에만
- 실무에서는 엔티티의 데이터는 조회할 일이 너무 많으므로 Getter의 경우 모두 열어두는 것이 편리하다.
- 하지만 Setter는.. 데이터가 변하기 때문에 추적 불가해지므로!

`jpabook.jpashop.domain' 패키지 생성 후 패키지 하에 엔티티 생성
## 회원 엔티티
- mappedBy : "member" orders의 member 필드에 의해서 매핑된 것! 연관관계 주인이 아님을 표시
- 읽기 전용, 여기 값을 변경한다고 반영되지 않는다.
- 객체는 변경포인트가 2군데, 테이블에서는 FK 하나만 변경하면 된다..! 둘 중 하나를 연관관계 주인
```java
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

```

## 주문 엔티티

## 주문 상태

## 주문상품 엔티티
- JPA는 ALTER 해서 FK 다 잡아준다