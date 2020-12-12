package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Device")
public class Device {

    private int id;
    private String name; //name of the device

    public Device() {
    }

    public Device(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        //change the name of the device
        this.name = name;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="smartHome_id")
    private SmartHome smartHome;

    public SmartHome getSmartHome() {
        return smartHome;
    }

    public void setSmartHome(SmartHome smartHome) {
        this.smartHome = smartHome;
    }
    
}
