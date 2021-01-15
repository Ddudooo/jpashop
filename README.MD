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

## Reference

* [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
* [Spring Boot JPA](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot Web](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-developing-web-applications)
* [Tailwind css](https://tailwindcss.com/)
* [Gradle Plugin for Node](https://github.com/node-gradle/gradle-node-plugin)