package com.springjpa.study.jpashop;

import com.springjpa.study.jpashop.domain.Address;
import com.springjpa.study.jpashop.domain.Delivery;
import com.springjpa.study.jpashop.domain.DeliveryStatus;
import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.domain.Order;
import com.springjpa.study.jpashop.domain.OrderItem;
import com.springjpa.study.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @Value("${spring.jpa.hibernate.ddl-auto:none}")
    private String ddlAuto;

    @PostConstruct
    public void init() {
        if (ddlAuto.equals("create")) {
            initService.init();
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void init() {
            List<Member> members = createMembers(1000);
            List<Book> books = createBooks(100);
            members
                .forEach(m -> {
                    int rndOrder = new Random().nextInt(4) + 1; //1 ~ 4
                    Set<Book> rndBook = new HashSet<>();
                    for (int i = 0; i < rndOrder; i++) {
                        rndBook.add(books.get(new Random().nextInt(books.size())));
                    }
                    createOrder(m, rndBook);
                });
        }

        public List<Member> createMembers(int count) {
            List<Member> members = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Member member = new Member();
                member.setName(getRandomMemberName());
                member.setAddress(getRandomAddress());
                em.persist(member);
                members.add(member);
            }
            return members;
        }

        public List<Book> createBooks(int count) {
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Book book = new Book();
                book.setName(getRandomString(10));
                book.setPrice((new Random().nextInt(100) + 1) * 1000);
                book.setStockQuantity((new Random().nextInt(100) + 1) * 1000);
                book.setAuthor(FirstName.randomFirstName() + LastName.randomLastName());
                book.setIsbn(
                    getRandomNumStr(3) + "-" +
                        getRandomNumStr(1) + "-" +
                        getRandomNumStr(6) + "-" +
                        getRandomNumStr(2) + "-" +
                        getRandomNumStr(1)
                );
                em.persist(book);
                books.add(book);
            }
            return books;
        }

        public void createOrder(Member member, Book book) {
            int rand = (new Random().nextInt(10) + 1);
            OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), rand);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            delivery.setStatus(DeliveryStatus.READY);
            Order order = Order.createOrder(member, delivery, orderItem);
            em.persist(order);
        }

        public void createOrder(Member member, Set<Book> books) {
            int rand = (new Random().nextInt(10) + 1);
            List<OrderItem> orderItems = new ArrayList<>();
            for (Book book : books) {
                OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), rand);
                orderItems.add(orderItem);
            }
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            delivery.setStatus(DeliveryStatus.READY);
            Order order = Order.createOrder(member, delivery,
                orderItems.toArray(new OrderItem[orderItems.size()]));
            em.persist(order);
        }

        public String getRandomMemberName() {
            return FirstName.randomFirstName() + LastName.randomLastName();
        }

        public Address getRandomAddress() {
            return new Address(KoreaCity.randomKoreaCity(), getRandomString(5),
                getRandomNumStr(3) + "-" + getRandomNumStr(3));
        }

        public String getRandomString(int size) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append(getRandomAlphabet());
            }
            return builder.toString();
        }

        public String getRandomNumStr(int size) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append(getRandomNumber());
            }
            return builder.toString();
        }

        protected String getRandomAlphabet() {
            return String.valueOf((char) ((int) (new Random().nextInt(26)) + 97));
        }

        protected String getRandomNumber() {
            return String.valueOf(new Random().nextInt(10));
        }

        static enum FirstName {
            KIM("김"),
            LEE("이"),
            PARK("박"),
            JUNG("정"),
            CHOI("최");

            private String hangleFirstName;

            FirstName(String hangleFirstName) {
                this.hangleFirstName = hangleFirstName;
            }

            @Override
            public String toString() {
                return hangleFirstName;
            }

            private static final List<FirstName> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
            private static final int SIZE = VALUES.size();
            private static final Random RANDOM = new Random();

            public static String randomFirstName() {
                return VALUES.get(RANDOM.nextInt(SIZE)).hangleFirstName;
            }
        }

        static enum LastName {
            MINJUN("민준"),
            SEOJUN("서준"),
            YEJUN("예준"),
            DOYOON("도윤"),
            SIU("시우"),
            JUWON("주원"),
            HAJUN("하준"),
            JIHO("지호"),
            JIHOO("지후"),
            JUNSEO("준서"),
            SEOYEON("서연"),
            SEOYOON("서윤"),
            JIWOO("지우"),
            SEOHYUN("서현"),
            MINSEO("민서"),
            HAEUN("하은"),
            HAYOON("하윤"),
            YOONSEO("윤서"),
            JIYU("지유"),
            JIMIN("지민");

            private String hangleLastName;

            LastName(String hangleLastName) {
                this.hangleLastName = hangleLastName;
            }

            @Override
            public String toString() {
                return hangleLastName;
            }

            private static final List<LastName> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
            private static final int SIZE = VALUES.size();
            private static final Random RANDOM = new Random();

            public static String randomLastName() {
                return VALUES.get(RANDOM.nextInt(SIZE)).hangleLastName;
            }
        }

        static enum KoreaCity {
            SEOUL("서울"),
            INCHEON("인천"),
            GUNSAN("군산"),
            MOKPO("목포"),
            DAEGU("대구"),
            BUSAN("부산"),
            CHANGWON("창원"),
            DAEJEON("대전"),
            JEONJU("전주"),
            GWANGJU("광주");

            private String cityName;

            KoreaCity(String cityName) {
                this.cityName = cityName;
            }

            @Override
            public String toString() {
                return cityName;
            }

            private static final List<KoreaCity> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
            private static final int SIZE = VALUES.size();
            private static final Random RANDOM = new Random();

            public static String randomKoreaCity() {
                return VALUES.get(RANDOM.nextInt(SIZE)).cityName;
            }
        }
    }
}