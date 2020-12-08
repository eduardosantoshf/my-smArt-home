package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password; //String or hash (depending on implementation might be better to use hash
    
    @Column(name = "phone_number", nullable = false)
    private String phone_number;

    public User(){}

    public User(String email,String username,String password, String phone_number){
        this.email=email;
        this.username=username;
        this.password=password;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPhoneNumber() {
        return phone_number;
    }

    public String getPassword() {
        return password;
    }
    
}
