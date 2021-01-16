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
     * 물품 정보 업데이트.
     *
     * @param id            물품 ID.
     * @param name          상품명.
     * @param price         가격.
     * @param stockQuantity 수량.
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(id);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
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