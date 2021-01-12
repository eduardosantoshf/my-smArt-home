package ua.mysmArthome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
public class User {

    private int id;
    private String email;
    private String username;
    private String password; //String or hash (depending on implementation might be better to use hash
    private String phone;
    private String token;
    private List<Integer> homes_id;

    public User() {
    }

    public User(String email, String username, String password, String phone_number) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone_number;
        this.homes_id = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "phone", nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone_number) {
        this.phone = phone_number;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "token",nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @ElementCollection

    public List<Integer> getHomes_id() {
        return homes_id;
    }

    public void setHomes_id(List<Integer> homes_id) {
        this.homes_id = homes_id;
    }

}
