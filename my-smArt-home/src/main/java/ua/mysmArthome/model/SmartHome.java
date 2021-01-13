package ua.mysmArthome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "SmartHome")
public class SmartHome {
    private int id;
    private String name;
    private List<Device> list_devices;
    private List<Logs> logs=new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SmartHome() {
    }

    public SmartHome(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "smarthome")
    @JsonIgnore
    public List<Logs> getLogs(){
        return this.logs;
    }

    public void setLogs(List<Logs> logs){
        this.logs=logs;
    }

    public void addLog(Logs log){
        this.logs.add(log);
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @OneToMany(mappedBy = "smarthome")
    @JsonIgnore
    public List<Device> getList_devices() {
        return list_devices;
    }

    public void setList_devices(List<Device> list_devices) {
        this.list_devices = list_devices;
    }

}
