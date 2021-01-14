package com.springjpa.study.jpashop.repo;

import com.springjpa.study.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 생성 및 업데이트.
     *
     * @param item 등록할 물품.
     */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    /**
     * 물품 단일 조회.
     *
     * @param id 조회할 ID.
     * @return 물품
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 물품 전부 조회.
     *
     * @return 조회된 물품들.
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }
}