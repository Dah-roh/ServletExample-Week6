package net.codejava.javaee.bookstore;

import lombok.Data;

@Data
public class BookDTO {
    protected int id;
    protected String title;
    protected String author;
    protected float price;
}
