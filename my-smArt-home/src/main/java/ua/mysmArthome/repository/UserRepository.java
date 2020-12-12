package ua.mysmArthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{
    
    User findById(int id);

    User findByUsername(String name);

    User findByEmail(String email);

    User findByToken(String token);
    
}
