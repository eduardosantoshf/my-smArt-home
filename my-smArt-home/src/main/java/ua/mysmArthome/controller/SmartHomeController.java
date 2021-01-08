/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.SmartHome;
import ua.mysmArthome.repository.SmartHomeRepository;
import ua.mysmArthome.repository.UserRepository;

@RestController
@RequestMapping("/smartHome")
public class SmartHomeController {

    @Autowired
    private SmartHomeRepository smartHomeRepository;

    @GetMapping("/all")
    public List<SmartHome> getAllSmartHomes() {
        return smartHomeRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SmartHome> getSmartHomebyId(@PathVariable int id) throws ResourceNotFoundException {
        SmartHome smartHome = smartHomeRepository.findHomeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmartHome " + id + " not found in SmartHome"));
        return ResponseEntity.ok().body(smartHome);
    }

    @PostMapping("/post/{name}")
    public String createSmartHome(@PathVariable String name) throws ResourceNotFoundException {
        SmartHome smartHome = smartHomeRepository.save(new SmartHome(name));
        System.out.println("{\"id\":"+smartHome.getId()+",\"name\":\""+smartHome.getName()+"\"}");
        return "{\"id\":"+smartHome.getId()+",\"name\":\""+smartHome.getName()+"\"}";
    }

    @DeleteMapping("/delete")
    public void deleteAll() {
        smartHomeRepository.deleteAll();
    }

}
