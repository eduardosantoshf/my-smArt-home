package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.Device;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>{
    
    List<Device> findById(int id);

    List<Device> findByName(String name);

    List<Device> findAll();

}
