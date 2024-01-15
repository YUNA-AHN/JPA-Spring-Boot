# 02. 상품 리포지토리 개발

## 상품 리포지토리 코드
- save()
  - id가 없으면 신규로 보고 persist() 실행
  - id가 있으면 이미 데이터베이스에 저장된 엔티티를 수정한다고 보고, merge()fmf tlfgod
```java
package jpabook.jpashop.respository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        // getId id값이 없다면 새로운 객체라는 것! => 신슈로 등록
        // 이미 있다면 업데이트
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // 업데이트 비슷
        }
    }
    
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
    
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}

```