/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.SmartHome;
import ua.mysmArthome.repository.AdminRepository;
import ua.mysmArthome.repository.SmartHomeRepository;

@RestController
@RequestMapping("/smartHome")
public class SmartHomeController {

    @Autowired
    private SmartHomeRepository smartHomeRepository;

    @Autowired
    private AdminRepository adminRepository;
    
    
    @GetMapping("/all")
    public List<SmartHome> getAllSmartHomes() {
        return smartHomeRepository.findAll();
    }
    @RequestMapping(value="/admin/{id}",method= RequestMethod.GET)
    public ResponseEntity<SmartHome> getSmartHomebyAdmin(@PathVariable int id) throws ResourceNotFoundException {
        SmartHome smartHome = smartHomeRepository.findSmartHomebyAdmin(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin "+id+" not found in SmartHome"));
        return ResponseEntity.ok().body(smartHome);
    }
    
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public ResponseEntity<SmartHome> getSmartHomebyId(@PathVariable int id) throws ResourceNotFoundException {
        SmartHome smartHome = smartHomeRepository.findHomeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmartHome "+id+" not found in SmartHome"));
        return ResponseEntity.ok().body(smartHome);
    }
    @PostMapping("/post/{id_admin}")
    public SmartHome createSmartHome(@PathVariable int id_admin,@Valid @RequestBody SmartHome smartHome) throws ResourceNotFoundException{
        return adminRepository.findById(id_admin).map(admin->{
            smartHome.setAdmin(admin);
            return smartHomeRepository.save(smartHome);
        }).orElseThrow(()-> new ResourceNotFoundException("Error"));
    }
    @DeleteMapping("/delete")
    public void deleteAll(){
        smartHomeRepository.deleteAll();
    }
    
}
