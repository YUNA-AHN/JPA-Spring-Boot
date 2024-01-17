package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.respository.ItemRepository;
import jpabook.jpashop.respository.MemberRepository;
import jpabook.jpashop.respository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 이거 추가 공부 필요함
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /** 주문 */
    // 주문은 데이터를 변경하는 것이기 때문에 트랜젝션
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성 : 회원 정보의 주소로 보냄 : 실무에서는 다시 한 번 입력을 받는다.
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성 : 드디어 사용하는 생성메서드 => orderitem 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // cascade 옵션 => order를 persist 하면 들어가있는 collection에 있는 Order Item도 강제로 persist를 날려준다.
        // order에서 orderItem, Delivery cascade.all 걸려있음 : 하나만 저장해줘도 자동으로 persist!
        // 참조하는게 딱 주인이 프라이빗 오너(다른 것이 참조할 수 없는)인 경우에만 사용
        // order만 delivery 사용하고, order만 orderitem 사용, persist 해야하는 life cycle도 동일해서 사용!
        orderRepository.save(order);
        return order.getId(); // order의 식별자값 반환
    }

    /** 취소 */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    /** 검색 */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findOne(orderSearch)''
//    }
}
