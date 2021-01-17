package com.springjpa.study.jpashop.api;

import com.springjpa.study.jpashop.domain.Address;
import com.springjpa.study.jpashop.domain.Order;
import com.springjpa.study.jpashop.domain.OrderStatus;
import com.springjpa.study.jpashop.repo.OrderRepository;
import com.springjpa.study.jpashop.repo.OrderSearch;
import com.springjpa.study.jpashop.repo.order.simplequery.OrderSimpleQueryDto;
import com.springjpa.study.jpashop.repo.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    /*
    실무에서 엔티티를 직접 노출하지 말것.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        //무한 루프.
        //타입 에러. -> 지연로딩의 경우 프록시 객체 조회로 가져옴. -> hibernate5Module
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //LAZY 강제초기화
            order.getDelivery().getAddress(); //LAZY 강제초기화
        }
        return all;
    }*/

    /**
     * LAZY로딩으로 인한 많은 테이블 조회.
     *
     * @return 조회데이터.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<SimpleOrderDto> simpleOrderDtos = orders.stream()
            .map(SimpleOrderDto::new).collect(Collectors.toList());
        return simpleOrderDtos;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> simpleOrderDtos = orders.stream()
            .map(SimpleOrderDto::new).collect(Collectors.toList());
        return simpleOrderDtos;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}