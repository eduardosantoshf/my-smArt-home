package ua.mysmArthome.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.mysmArthome.model.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{
    
    /*List<User> findById(int id);

    List<User> findByUsername(String name);

    List<User> findByEmail(String email);*/
    
    @Query("Select t from User t where t.id=:id")
    Optional<User> findUserById(@Param("id") int id);
    
    @Query("Select t from User t where t.username=:username")
    Optional<User> findUserByUsername(@Param("username") String username);
    
    @Query("Select t from User t where t.email=:email")
    Optional<User> findUserByEmail(@Param("email") String email);
    
    @Query(value="Select t from User t where t.admin.id=:id")
    List<User> findUserByAdminId(@Param("id") int id);
}
