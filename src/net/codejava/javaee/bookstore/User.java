package net.codejava.javaee.bookstore;

import lombok.Data;

@Data

public class User {
    protected Long id;
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }
}
