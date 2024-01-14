# 03. 엔티티 클래스 개발
예제에서는 엔티티 클랫에 Getter, Setter를 모두 열고 단순히 설계 예정!
- 실무에서는 가급적 Getter는 열어두고, Setter는 꼭 필용한 경우에만
- 실무에서는 엔티티의 데이터는 조회할 일이 너무 많으므로 Getter의 경우 모두 열어두는 것이 편리하다.
- 하지만 Setter는.. 데이터가 변하기 때문에 추적 불가해지므로!

`jpabook.jpashop.domain' 패키지 생성 후 패키지 하에 엔티티 생성
## 회원 엔티티
- mappedBy : "member" orders의 member 필드에 의해서 매핑된 것! 연관관계 주인이 아님을 표시
- 읽기 전용, 여기 값을 변경한다고 반영되지 않는다.
- 객체는 변 경포인트가 2군데, 테이블에서는 FK 하나만 변경하면 된다..! 둘 중 하나를 연관관계 주인
- 참고
  - @Column(name="member_id") : table_id
  - 객체는 어디 소속인지 명확하지만, 테이블은 단순하게 id라고 하면 쉽지 않음, FK와 맞춰주는 것
  - 엔티티의 식별자는 id를, PK 컬럼명은 member_id를 사용
  - 엔티티는 타입이 있으므로 id 필드만으로 구분 가능, 테이블은 타이빙 없으므로 구분이 어려움!
  - 관례상 테이블명 + id 많이 사용. 객체에서 memberId 사용해도 괜찮다! **일관성이 중요**
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

## 주문 엔티티 : Order
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

}

```

## 주문 상태 : OrderStatus
```java
package jpabook.jpashop.domain;

public enum OrderStatus {
    ORDER, CANCEL
}
```

## 주문상품 엔티티
- JPA는 ALTER 해서 FK 다 잡아준다
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
}
```

## 상품 엔티티
```java
package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
// InheritanceType : SINGLE_TABLE, TABLE_PER_CLASS, JOINED;
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // Book, Album, Movie 따로 지정해주지 않아도 알아서 잘 들어간다
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();
}
```

## 상품 - 도서
```java
package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
public class Book extends Item{
    private String author;
    private String isbn;
}
```
## 상품 - 음반
```java
package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item{
    private String artist;
    private String etc;
}

```
## 상품 - 영화
```java
package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Movie extends Item{
    private String director;
    private String actor;
}
```

## 배송 엔티티
```java
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

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // 기본인 ORDINARY로 넣는 경우 값 추가시 꼬일 수 있으므로 무조건 STRING!
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP

}
```

## 배송 상태
```java
package jpabook.jpashop.domain;

public enum
DeliveryStatus {
    READY, COMP
}

```
## 카테고리
- 실무에서는 ManyToMay 사용하지 말것
- 중간 테이블 처럼 컬럼을 추가할 수도, 커리를 세미랗게 실행하기도 어렵기 때문
- 중간 엔티티를 생성하여 매핑하여 사용하는 것을 추천!
```java
package jpabook.jpashop;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // 실무에서 잘 사용하지 않는 이유 : 필드 추가가 되지 않음
    @ManyToMany
    @JoinTable(name = "categoty_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name= "Item_id")) // 중간 테이블을 매핑해주어야 한다.
    private List<Item> items = new ArrayList<>();

    // 셀프로 양방향 연관관계 걸어준 것
    // 자식-엄마 : 여러 명의 자식은 하나의 엄마
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 부모-자식 : 자식은 여러개
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    
    // 연관관계 메서드 //
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
```

## 주소 값 타입
- 값 타입은 변경이 불가능하게 설계해야한다.
- Setter를 제거하고, 생성자에서 값을 모두 초기화 해서 변경 불가능한 클래스를 만들것 !
- JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자(default constructor) => public or protected로 설정
- JPA 구현라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문
```java
package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;

    }

}
```


### 강의 다 듣고 추가로 공부하고 싶은 부분 !!!!
- @annotation들 생각보다 다양함.. Column, Embedded 등등 조금 자료를 찾아보자
- 값 타입에 대한 부분 protected private public 등
- 엔티티와 테이블?
- cascadeType, fetchType
- 주문과 카테고리의 연관관계 메서드 이후로 하나도 모르겠음 공부 필요 ㅠㅠ
- InheritanceType : SINGLE_TABLE, TABLE_PER_CLASS, JOINED;
- public abstract