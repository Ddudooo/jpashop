package com.springjpa.study.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 앨범 엔티티.
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
    private String etc;
}