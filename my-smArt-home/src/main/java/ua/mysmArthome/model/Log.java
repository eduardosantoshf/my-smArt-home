package ua.mysmArthome.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Log")
public class Log {
    private int id;
    private String value;
    private Device device;
    private LocalDateTime data;

    public Log() {
        this.data = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "data", nullable = false)
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Column(name = "value", nullable = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToOne
    @JoinColumn(name="device_id")
    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device deviceId) {
        this.device = deviceId;
    }

    /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    System.out.println(dtf.format(now));*/
}