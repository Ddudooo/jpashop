package com.springjpa.study.jpashop.repo.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
            "select "
                + "new com.springjpa.study.jpashop.repo.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) "
                + "from Order o "
                + "join o.member m "
                + "join o.delivery d", OrderSimpleQueryDto.class)
            .getResultList();
    }
}