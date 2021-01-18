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
import ua.mysmArthome.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    //List<Device> findById(int id);

    @Query("Select t from Notification t where t.id=:id")
    Optional<Notification> findNotificationBy(@Param("id") int id);

    @Query("Select t from Notification t where t.data>:data")
    Optional<Notification> findNotificationsByLowerData(@Param("data") LocalDateTime data);

    @Query("Select t from Notification t where t.data<:data")
    Optional<Notification> findNotificationsByHigherData(@Param("data") LocalDateTime data);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification t WHERE t.device=:device")
    void deleteAllByDevice(@Param("device") Device device);
}