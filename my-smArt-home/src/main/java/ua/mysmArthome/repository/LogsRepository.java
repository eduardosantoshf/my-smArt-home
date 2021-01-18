package ua.mysmArthome.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import ua.mysmArthome.model.Device;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.mysmArthome.model.Log;

@Repository
public interface LogsRepository extends JpaRepository<Log, Integer>{
    //List<Device> findById(int id);

    @Query("Select t from Log t where t.id=:id")
    Optional<Log> findLogBy(@Param("id") int id);

    @Query("Select t from Log t where t.data>:data")
    Optional<Log> findLogByLowerData(@Param("data") LocalDateTime data);

    @Query("Select t from Log t where t.data<:data")
    Optional<Log> findLogByHigherData(@Param("data") LocalDateTime data);

    @Transactional
    @Modifying
    @Query("DELETE FROM Log t WHERE t.device=:device")
    void deleteAllByDevice(@Param("device") Device device);
}