package ua.mysmArthome.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.Device;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>{
    
    //List<Device> findById(int id);

    @Query("Select t from Device t where t.name=:name")
    Optional<Device> findDeviceByName(@Param("name") String name);
    
    @Query("Select t from Device t where t.smarthome.id=:id")
    Optional<Device> findDevicesBySmartHomeId(@Param("id") int id);

    List<Device> findAll();
}
