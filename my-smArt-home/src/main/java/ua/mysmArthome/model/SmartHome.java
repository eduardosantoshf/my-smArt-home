package ua.mysmArthome.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "SmartHome")
public class SmartHome {
    private int id;
    private Admin admin;
    private List<Device> list_devices;

    //private Admin admin;
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
    @OneToOne
    @JoinColumn(name = "admin_id")
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @OneToMany(mappedBy = "smarthome")
    public List<Device> getList_devices() {
        return list_devices;
    }

    public void setList_devices(List<Device> list_devices) {
        this.list_devices = list_devices;
    }
    
    
}
