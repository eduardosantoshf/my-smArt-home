package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.SmartHome;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SmartHomeRepository  extends JpaRepository<SmartHome, Integer>{
    
    @Query("Select t from SmartHome t where t.id=:id")
    Optional<SmartHome> findHomeById(@Param("id") int id);
    @Query("Select t from SmartHome t where t.admin_id=:id")
    Optional<SmartHome> findSmartHomebyAdmin(@Param("id") int id);
}
