package ua.mysmArthome.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.Device;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.mysmArthome.model.LogDevice;

@Repository
public interface LogDeviceRepository extends JpaRepository<LogDevice, Integer>{

    //List<Device> findById(int id);

    @Query("Select t from LogDevice t where t.id=:id")
    Optional<Device> findLogDeviceById(@Param("id") int id);

}
