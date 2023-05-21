package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter @Setter
public class BookForm {

    private Long id;
    @NotEmpty
    private String name;
    @PositiveOrZero
    private int price;
    @Positive
    private int stockQuantity;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

}
