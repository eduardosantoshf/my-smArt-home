package ua.mysmArthome.controller;

import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.*;
import ua.mysmArthome.rabbitmq.consumer.Consumer;
import ua.mysmArthome.rabbitmq.producer.RpcProducer;
import ua.mysmArthome.repository.*;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SmartHomeRepository smartHomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private LogsRepository logsRepository;

    private Consumer consumer = new Consumer();

    public NotificationController() throws ResourceNotFoundException {
    }

    @CrossOrigin
    @GetMapping("/getAll/{username}")
    public String getAll(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        loadNotifications();loadLogs();
        String id="";
        Integer home_id = userRepository.findHomesByUsername(username).getHomes_id().get(0);
        id=String.valueOf(home_id);

        // obtain all notifications from devices
        List<Notification> notificacoes = new ArrayList<>();
        SmartHome sm = smartHomeRepository.findHomeById(home_id).orElseThrow(() -> new ResourceNotFoundException("Home " + home_id + " not found"));

        for(Device d : sm.getList_devices()){
            for(Notification n : d.getList_notifications()){
                notificacoes.add(n);
            }
        }

        // create return message
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String retorno = "{\"notificacoes\":[";
        int counter=0;
        for(Notification n : notificacoes){
            counter++;

            retorno+="{\"deviceId\":\""+String.valueOf(n.getDevice().getInBroker_id())+"\", \"value\":\""+n.getValue()+"\", \"date\":\""+dtf.format(n.getData())+"\"}";

            if(counter<notificacoes.size())
                retorno+=",";
        }
        retorno+="]}";
        for(Device d : sm.getList_devices()){
            d.clearNotifications();
            deviceRepository.save(d);
            notificationRepository.deleteAllByDevice(d);
        }

        return retorno;
    }

    private void loadNotifications() throws ResourceNotFoundException {

        Map<String, ArrayList<String>> notifications = consumer.getNotifications();

        for(String device_id : notifications.keySet()){
            Device d = deviceRepository.findDeviceByInBrokerId(Integer.valueOf(device_id)).orElseThrow(() -> new ResourceNotFoundException("Device "+device_id+" not found"));

            for(String s : notifications.get(device_id)){
                Notification n = new Notification();
                n.setData(LocalDateTime.now());
                n.setDevice(d);
                n.setValue("New alarm: " + s);
                notificationRepository.save(n);

                d.addListNotification(n);

                deviceRepository.save(d);
            }
        }
    }

    private void loadLogs() throws ResourceNotFoundException {

        Map<String, ArrayList<String>> logs = consumer.getLogs();

        for(String device_id : logs.keySet()){
            Device d = deviceRepository.findDeviceByInBrokerId(Integer.valueOf(device_id)).orElseThrow(() -> new ResourceNotFoundException("Device "+device_id+" not found"));

            for(String s : logs.get(device_id)){
                Log l = new Log();
                l.setDevice(d);
                l.setData(LocalDateTime.now());
                l.setValue(s);
                logsRepository.save(l);

                d.addListLogs(l);

                deviceRepository.save(d);
            }
        }
    }

    public LocalDateTime getCurrentTime(){
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        //return dtf.format(now);
        return now;
    }
}
