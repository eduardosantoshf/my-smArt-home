package ua.mysmArthome.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private SmartHome smarthome;
    private int inBroker_id;

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

    @Column(name = "inBroker_id", nullable = false)
    public int getInBroker_id() {
        return inBroker_id;
    }

    public void setInBroker_id(Integer id) {
        //change the name of the device
        this.inBroker_id=id;
    }

    @ManyToOne
    @JoinColumn(name="smartHome_id")

    public SmartHome getSmarthome() {
        return smarthome;
    }

    public void setSmarthome(SmartHome smarthome) {
        this.smarthome = smarthome;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", name=" + name + ", smarthome=" + smarthome + '}';
    }
    
}
