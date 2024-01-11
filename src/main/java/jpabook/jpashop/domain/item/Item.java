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
    private List<Category> categories = new ArrayList<>();
}
