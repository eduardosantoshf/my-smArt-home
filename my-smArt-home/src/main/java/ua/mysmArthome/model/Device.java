package ua.mysmArthome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Device")
public class Device {

    private int id;
    private String name; //name of the device
    private SmartHome smarthome;
    private int inBroker_id;
    private List<Log> logs;
    private List<Notification> list_notifications;

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

    @OneToMany(mappedBy = "device", cascade=CascadeType.REMOVE)
    @JsonIgnore
    public List<Log> getLogs(){
        return this.logs;
    }
    public void setLogs(List<Log> logs){
        this.logs=logs;
    }

    public void addListLogs(Log l){ this.logs.add(l); }

    @OneToMany(mappedBy = "device", cascade=CascadeType.REMOVE)
    @JsonIgnore
    public List<Notification> getList_notifications() {
        List<Notification> n = this.list_notifications;
        return n;
    }
    public void setList_notifications(List<Notification> list_devices) {
        this.list_notifications = list_devices;
    }

    public void addListNotification(Notification n){
        this.list_notifications.add(n);
    }

    public void clearNotifications(){
        this.list_notifications = new ArrayList<>();
    }
}
