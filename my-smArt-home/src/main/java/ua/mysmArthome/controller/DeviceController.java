package ua.mysmArthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.mysmArthome.model.Device;
import ua.mysmArthome.repository.DeviceRepository;

import java.util.List;

@Controller
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/devices")
    public String getDevices(){
        //get all devices
        List<Device> devices = deviceRepository.findAll();
        //create appropriate string for the devices
        String devicesStr="";
        for (int i=0; i<devices.size()-1;i++){
            Device device = devices.get(i);
            devicesStr+="{\"id\":\""+device.getId()+"\",\"name\":\""+device.getName()+"\"},";
        }
        devicesStr+="{\"id\":\""+devices.get(devices.size()-1).getId()+"\",\"name\":\""+devices.get(devices.size()-1).getName()+"\"}";
        //
        return "{\"devices\":["+devicesStr+"]}";
    }
    
}
