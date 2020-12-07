package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.User;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{
    
    List<User> findById(int id);

    List<User> findByUsername(String name);

    List<User> findByEmail(String email);
}
