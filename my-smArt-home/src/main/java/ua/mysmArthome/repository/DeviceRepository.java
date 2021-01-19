package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.Device;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>{
    
    @Query("Select t from Device t where t.id=:id")
    Optional<Device> findDeviceById(@Param("id") int id);

    @Query("Select t from Device t where t.inBroker_id=:id")
    Optional<Device> findDeviceByInBrokerId(@Param("id") int id);

    @Query("Select t from Device t where t.name=:name")
    Optional<Device> findDeviceByName(@Param("name") String name);
    
    @Query("Select t from Device t where t.smarthome.id=:id")
    Optional<Device> findDevicesBySmartHomeId(@Param("id") int id);

}
