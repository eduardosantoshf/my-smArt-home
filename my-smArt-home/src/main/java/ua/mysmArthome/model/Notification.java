package ua.mysmArthome.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
public class Notification {

    private int id;
    private String value;
    private Device device;
    private LocalDateTime data;
    private boolean seen;


    public Notification() {
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

    @Column(name = "seen", nullable = false)
    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @ManyToOne
    @JoinColumn(name="device_id")
    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device deviceId) {
        this.device = deviceId;
    }

}
