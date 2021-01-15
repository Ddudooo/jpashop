package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.Delivery;
import com.springjpa.study.jpashop.domain.DeliveryStatus;
import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.domain.Order;
import com.springjpa.study.jpashop.domain.OrderItem;
import com.springjpa.study.jpashop.domain.item.Item;
import com.springjpa.study.jpashop.repo.ItemRepository;
import com.springjpa.study.jpashop.repo.MemberRepository;
import com.springjpa.study.jpashop.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 서비스.
 * <p>
 * 비지니스 로직은 엔티티에서 처리.
 * </p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문.
     *
     * @return 주문 ID.
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member findMember = memberRepository.findOne(memberId);
        Item findItem = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(findMember, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소.
     *
     * @param orderId 주문 ID.
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);

        order.cancel();
    }

    //검색
    /*public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }*/
}