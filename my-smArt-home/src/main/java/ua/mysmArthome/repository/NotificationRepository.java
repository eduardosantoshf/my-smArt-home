package ua.mysmArthome.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}