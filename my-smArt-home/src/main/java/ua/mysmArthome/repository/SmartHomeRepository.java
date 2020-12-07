package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.SmartHome;

import java.util.List;

@Repository
public interface SmartHomeRepository  extends JpaRepository<SmartHome, Long>{
    
    List<SmartHome> findById(int id);

    List<SmartHome> findById_User(int id);

}
