package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.item.Item;
import com.springjpa.study.jpashop.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 물품 저장 및 업데이트.
     *
     * @param item 저장할 물품.
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 물품들 조회.
     *
     * @return 조회된 물품들.
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     * 물품 조회.
     *
     * @param itemId 조회할 물품의 ID.
     * @return 물품.
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}