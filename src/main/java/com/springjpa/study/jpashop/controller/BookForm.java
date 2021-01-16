package com.springjpa.study.jpashop.controller;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm {

    private Long id;

    @NotEmpty
    private String name;
    @Min(0)
    private int price;
    @Min(1)
    private int stockQuantity;

    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;
}