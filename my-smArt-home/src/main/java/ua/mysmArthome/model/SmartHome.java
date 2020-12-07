package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "User")
public class SmartHome {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(mappedBy = "User")
    @Column(name = "id_User", nullable = false)
    private int id_User;

    
    public int getId() {
        return id;
    }
  
    public int getIdUser() {
        return id_User;
    }

}
