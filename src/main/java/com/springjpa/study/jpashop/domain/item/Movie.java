package com.springjpa.study.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 영화 엔티티.
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("C")
public class Movie extends Item {

    private String director;
    private String actor;
}