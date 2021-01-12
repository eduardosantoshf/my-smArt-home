package ua.mysmArthome.model;

import javax.persistence.*;

@Entity
@Table(name = "Logs")
public class Logs {
    private int id;
    private SmartHome smarthome;
    private String timedate;
    private String text;

    public Logs() {

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
    @JoinColumn(name="smartHome_id")

    public SmartHome getSmarthome() {
        return smarthome;
    }

    public void setSmarthome(SmartHome smarthome) {
        this.smarthome = smarthome;
    }
}
