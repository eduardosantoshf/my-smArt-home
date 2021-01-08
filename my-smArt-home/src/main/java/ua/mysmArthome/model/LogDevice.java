package ua.mysmArthome.model;

import javax.persistence.*;

@Entity
@Table(name = "LogDevice")
public class LogDevice {
    private int id;
    private Device device;
    private String timedate;
    private String text;

    public LogDevice() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "text", nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "timedate", nullable = false)
    public String getTimedate() {
        return timedate;
    }

    public void setTimedate(String timedate) {
        this.timedate = timedate;
    }

    @ManyToOne
    @JoinColumn(name="device")

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
