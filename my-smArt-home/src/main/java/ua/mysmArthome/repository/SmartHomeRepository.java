package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.SmartHome;


@Repository
public interface SmartHomeRepository  extends JpaRepository<SmartHome, Integer>{

}
