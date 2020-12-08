package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Device")
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name; //name of the device

    @Column(name = "type", nullable = false)
    private String type; //type of the device

    @Column(name = "status", nullable = false)
    private boolean status=false; //indicates if its on or off

    public Device(){}

    public Device(String name){
        this.name=name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getType(){
        return type;
    }

    public void setName(String name) {
        //change the name of the device
        this.name = name;
    }

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
