package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.Device;
import ua.mysmArthome.model.SmartHome;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SmartHomeRepository  extends JpaRepository<SmartHome, Integer>{
    
    @Query("Select t from SmartHome t where t.id=:id")
    Optional<SmartHome> findHomeById(@Param("id") int id);


    @Modifying
    @Query("update SmartHome t set t.list_devices = ?1 where t.id = ?2")
    void deleteDevicesByHome(List<Device> devices, Integer id);
}
