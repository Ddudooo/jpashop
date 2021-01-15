package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.Address;
import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.domain.Order;
import com.springjpa.study.jpashop.domain.OrderStatus;
import com.springjpa.study.jpashop.domain.item.Book;
import com.springjpa.study.jpashop.exception.NotEnoughStockException;
import com.springjpa.study.jpashop.repo.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    void order() {
        //given
        Member member = createMember("회원");

        Book book = createBook("JPA test", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getOrderStatus());
        assertEquals("주문 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }

    @Test
    @DisplayName("상품 주문 - 재고 수량 초과")
    void orderExceptionForIllegalStateException() {
        //given
        Member member = createMember("회원");
        Book book = createBook("JPA test", 10000, 10);

        int orderCount = 11;

        //when
        NotEnoughStockException thrown = assertThrows(NotEnoughStockException.class,
            () -> orderService.order(member.getId(), book.getId(), orderCount)
        );
        //then
        assertEquals("재고 수량 부족 예외가 발생해야 한다.", NotEnoughStockException.class, thrown.getClass());
    }

    @Test
    @DisplayName("상품 취소")
    void cancelOrder() {
        //given
        Member member = createMember("test");
        Book book = createBook("JPA TEST", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getOrderStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
    }


    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}