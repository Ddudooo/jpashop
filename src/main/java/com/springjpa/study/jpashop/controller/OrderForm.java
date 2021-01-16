package com.springjpa.study.jpashop.controller;

import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderForm {

    private Long id;

    private List<Member> members = new ArrayList<>();
    @NotNull
    private Long memberId;
    private List<Item> items = new ArrayList<>();
    @NotNull
    private Long itemId;
    @Min(1)
    private int count;

    public OrderForm(List<Member> members, List<Item> items) {
        this.members = members;
        this.items = items;
    }
}