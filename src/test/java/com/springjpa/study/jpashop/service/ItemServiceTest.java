package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.item.Album;
import com.springjpa.study.jpashop.domain.item.Book;
import com.springjpa.study.jpashop.domain.item.Item;
import com.springjpa.study.jpashop.domain.item.Movie;
import com.springjpa.study.jpashop.repo.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("물품 저장 테스트")
    public void saveItem() {
        //given
        Book book = new Book();
        book.setName("new book");
        book.setAuthor("test author");
        book.setIsbn("some isbn");
        book.setPrice(500);
        book.setStockQuantity(1000);
        //when
        itemService.saveItem(book);
        //then
        assertEquals(book.getId(), itemRepository.findOne(book.getId()).getId());
    }

    @Test
    @DisplayName("물품 조회 테스트")
    public void findAllItems() {
        //given
        Book book = new Book();
        book.setName("book");
        book.setPrice(100);
        book.setStockQuantity(1000);
        itemService.saveItem(book);
        Album album = new Album();
        album.setName("album");
        album.setPrice(100);
        album.setStockQuantity(1000);
        itemService.saveItem(album);
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setPrice(100);
        movie.setStockQuantity(1000);
        itemService.saveItem(movie);
        //when
        List<Item> items = itemService.findItems();
        //then
        assertEquals(3, items.size());
        assertThat(items).contains(book, album, movie);
    }

    @Test
    @DisplayName("단일 물품 조회 테스트")
    public void findOneItem() {
        //given
        Book book = new Book();
        book.setName("book");
        book.setPrice(100);
        book.setStockQuantity(1000);
        itemService.saveItem(book);
        //when
        Item findBook = itemRepository.findOne(book.getId());
        //then
        assertEquals(findBook.getId(), book.getId());
    }
}