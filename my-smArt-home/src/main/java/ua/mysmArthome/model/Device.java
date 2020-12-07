package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "devices")
public class Device {
    
    private int id;
    private String name; //name of the device
    private boolean status=false; //indicates if its on or off

    public Device(){}

    public Device(String name){
        this.name=name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        //change the name of the device
        this.name = name;
    }

    @Column(name = "status", nullable = false)
    public boolean getStatus() {
        return status;
    }

    public void changeStatus(boolean status) {
        //turn on or off
        if (this.status)
            this.status=false;
        else
            this.status=true;
    } 
}
