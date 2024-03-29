package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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

    // 비즈니스 로직 : 재고를 늘리고 줄이는 것//
    // 도메인 주도 설계, 엔티티 자체가 해결할 수 있는 것은 엔티티 안에 비즈니스 로직 넣는 것이 객체 지향적

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stck");
        }
        this.stockQuantity = restStock;
    }
}
