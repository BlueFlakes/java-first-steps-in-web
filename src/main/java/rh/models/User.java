package rh.models;


import lombok.Getter;

@Getter
public class User {
    private Integer id;
    private String name;
    private String email;
    private String login;
    private String password;

    public User(Integer id, String name, String email, String login, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
    }
}
