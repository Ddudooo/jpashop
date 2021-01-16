# 스프링 부트 & JPA

스프링 부트와 JPA를 활용한 간단한 웹 애플리케이션 예제

## Environments

`JDK - openjdk 11`

`Build tool - Gradle`

## Memo

### 엔티티 설계시 주의

#### 엔티티에는 가급적 Setter를 사용하지 말자

Setter가 모두 열려있다. 변경 포인트가 너무 많아서, 유지보수가 어렵다. 나중에 리펙토링으로 Setter 제거

#### 모든 연관관계는 지연로딩으로 설정!

- 즉시로딩( EAGER )은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생
- 실무에서 모든 연관관계는 지연로딩( LAZY )으로 설정
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용
- `@XToOne`(`OneToOne`, `ManyToOne`) 관계는 기본이 즉시로딩 → 직접 지연로딩으로 설정

#### 컬렉션은 필드에서 초기화

컬렉션은 필드에서 바로 초기화 하는 것이 안전

- null 문제에서 안전하다.
- 하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.
- 만약 `getOrders()` 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.
  **따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.**

##### 테이블, 컬럼명 생성 전략

스프링 부트에서 하이버네이트 기본 매핑 전략을 변경해서 실제 테이블 필드명은 다름

[Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#howto-configure-hibernate-naming-strategy)

[Hibernate ORM 5.4.27.Final User Guide](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#naming)

하이버네이트 기존 구현: 엔티티의 필드명을 그대로 테이블 명으로 사용
`SpringPhysicalNamingStrategy`

스프링 부트 신규 설정 (엔티티(필드) 테이블(컬럼))

1. 카멜 케이스 언더스코어(`memberPoint` `member_point`)
2. .(점) _(언더스코어)
3. 대문자 소문자

**적용 2 단계**

1. 논리명 생성 명시적으로 컬럼, 테이블명을 직접 적지 않으면 `ImplicitNamingStrategy` 사용
   `spring.jpa.hibernate.naming.implicit-strategy`
   테이블이나, 컬럼명을 명시하지 않을 때 논리명 적용
2. 물리명 적용
   `spring.jpa.hibernate.naming.physical-strategy`
   모든 논리명에 적용됨, 실제 테이블에 적용 (`username` `usernm` 등으로 회사 룰로 바꿀 수 있음)

**스프링 부트 기본 설정**

```yaml
spring.jpa.hibernate.naming.implicit-strategy:
  org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
spring.jpa.hibernate.naming.physical-strategy:
  org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
```

### 참고 ###

폼 객체 vs 엔티티 직접 사용

> 요구사항이 정말 단순할 때는 폼 객체( MemberForm ) 없이
> 엔티티( Member )를 직접 등록과 수정 화면 에서 사용해도 된다.
> 하지만 화면 요구사항이 복잡해지기 시작하면, 엔티티에 화면을 처리하기 위한 기능이 점점 증가한다.
> 결과적으로 엔티티는 점점 화면에 종속적으로 변하고,
> 이렇게 화면 기능 때문에 지저분해진 엔티티는 결국 유지보수하기 어려워진다.

**실무에서 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다.**

화면이나 API에 맞는 폼 객체나 DTO를 사용하자. 그래서 화면이나 API 요구사항을 이것들로 처리하고, 엔티티는 최대한 순수 하게 유지하자.

__변경 감지와 병합__

**준영속 엔티티?**

영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.

여기서는 `itemService.saveItem(book)` 에서 수정을 시도하는 Book 객체.  
`Book` 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다.  
이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.

**준영속 엔티티를 수정하는 2가지 방법**

* 변경 감지 기능 사용
* 병합( `merge` ) 사용

**변경 감지 기능**

```java
@Transactional
void update(Item itemParam){ //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    Item findItem=em.find(Item.class,itemParam.getId()); //같은 엔티티를 조회한
    다.
    findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다.
    }
```

**병합 사용**

```java
@Transactional
void update(Item itemParam){ //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
    Item mergeItem=em.merge(item);
    }
```

**병합 동작 방식**

1. `merge()`를 실행한다.
2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.  
   2-1. 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
3. 조회한 영속 엔티티( `mergeMember` )에 `member` 엔티티의 값을 채워 넣는다.  
   `member` 엔티티의 모든 값을 `mergeMember`에 밀어 넣는다.  
   이때 `mergeMember`의 “회원1”이라는 이름이 “회원명변경”으로 바뀐다.
4. 영속 상태인 `mergeMember`를 반환한다.

**병합시 동작 방식을 간단히 정리**

1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체한다.(병합한다.)
3. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 UPDATE SQL이 실행

> 주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다. 병합시 값이 없으면 null 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)

**엔티티를 변경할 때는 항상 변경 감지를 사용하세요**

* 컨트롤러에서 어설프게 엔티티를 생성하지 말것.
* 트랜잭션이 있는 서비스 계층에 식별자(`id`)와 변경할 데이터를 명확하게 전달할것.(파라미터 or dto)
* 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경할것.
* 트랜잭션 커밋 시점에 변경 감지가 실행.

## Reference

* [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
* [Spring Boot JPA](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot Web](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-developing-web-applications)
* [Tailwind css](https://tailwindcss.com/)
* [Gradle Plugin for Node](https://github.com/node-gradle/gradle-node-plugin)