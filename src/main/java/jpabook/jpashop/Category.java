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
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 부모-자식 : 자식은 여러개
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
}
