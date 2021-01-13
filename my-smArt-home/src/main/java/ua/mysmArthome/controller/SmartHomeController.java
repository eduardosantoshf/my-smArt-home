/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.Device;
import ua.mysmArthome.model.SmartHome;
import ua.mysmArthome.rabbitmq.consumer.Consumer;
import ua.mysmArthome.repository.DeviceRepository;
import ua.mysmArthome.repository.SmartHomeRepository;

@RestController
@RequestMapping("/smartHome")
public class SmartHomeController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SmartHomeRepository smartHomeRepository;

    private Consumer consumer = new Consumer();

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


    @CrossOrigin
    @GetMapping("/notifications/{id}")
    public String getAllNotifications(@PathVariable int id) throws ResourceNotFoundException{
        SmartHome smartHome = smartHomeRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("SmartHome " + id + " not found in SmartHome"));
        System.out.println("WAS CALLED");
        String list_of_notifications = "{";

        for(Device device: smartHome.getList_devices()){
            list_of_notifications +="\"notification\":\"";

            System.out.println(device.getName()+" "+device.getId());
            ArrayList<String> nots = consumer.getNotifications(""+device.getInBroker_id());
            System.out.println(nots);
            for (String not: nots){
                list_of_notifications+=not;

                String logs = device.getLogs();
                logs="<p>[LOG AT "+getCurrentTime()+"] "+not+"</p>" + logs;
                if(logs.length()> 4000)
                    logs="<p>[LOG AT "+getCurrentTime()+"] logs cleaned</p>";
                device.setLogs(logs);
                deviceRepository.save(device);

                System.out.println("Qual a notificação: "+not);
            }

            smartHome.getList_devices().remove(device);
        }
        list_of_notifications+="\"}";
        return list_of_notifications;
    }

    public String getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
