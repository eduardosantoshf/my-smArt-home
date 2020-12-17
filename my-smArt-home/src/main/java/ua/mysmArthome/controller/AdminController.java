/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.Admin;
import ua.mysmArthome.repository.AdminRepository;

/**
 *
 * @author oscar
 */
@RestController
@RequestMapping("/user")
public class AdminController {
    @Autowired
    private AdminRepository userRepository;
    
    //i think the best way to find the users are username
    
    @GetMapping("/all")
    public List<Admin> getAllUsers() {
        return userRepository.findAll();
    }
    @RequestMapping(value="/usr/{username}",method= RequestMethod.GET)
    public ResponseEntity<Admin> getUserbyUsername(@PathVariable String username) throws ResourceNotFoundException {
        Admin user = userRepository.findAdminByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));
        return ResponseEntity.ok().body(user);
    }
    @RequestMapping(value="/email/{email}",method= RequestMethod.GET)
    public ResponseEntity<Admin> getDevicebyName(@PathVariable String email) throws ResourceNotFoundException {
        Admin user = userRepository.findAdminByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email "+email+" not found"));
        return ResponseEntity.ok().body(user);
    }
    
    @PostMapping("/")
    public Admin createUser(@Valid @RequestBody Admin user ){
        return userRepository.save(user);
    }
    
    @PutMapping("/{username}")
    public ResponseEntity<Admin> updateUsername(@PathVariable(value="username") String username,@Valid @RequestBody Admin userDetails)
        throws ResourceNotFoundException{
        Admin user = userRepository.findAdminByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User "+username+" not found"));
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setPhone(userDetails.getPhone());
        final Admin f_user = userRepository.save(user);
        return ResponseEntity.ok(f_user);
    }
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String username)
         throws ResourceNotFoundException {
        Admin user = userRepository.findAdminByUsername(username)
       .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}