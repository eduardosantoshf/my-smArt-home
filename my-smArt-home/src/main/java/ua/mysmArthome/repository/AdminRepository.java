/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.mysmArthome.model.Admin;

/**
 *
 * @author oscar
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
    @Query("Select t from User t where t.id=:id")
    Optional<Admin> findAdminById(@Param("id") int id);
    
    @Query("Select t from User t where t.username=:username")
    Optional<Admin> findAdminByUsername(@Param("username") String username);
    
    @Query("Select t from User t where t.email=:email")
    Optional<Admin> findAdminByEmail(@Param("email") String email);
}
