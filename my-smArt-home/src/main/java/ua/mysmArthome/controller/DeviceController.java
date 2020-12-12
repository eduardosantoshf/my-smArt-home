package ua.mysmArthome.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import ua.mysmArthome.model.Device;
import ua.mysmArthome.repository.DeviceRepository;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevicebyId(@PathVariable(value="id") int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findDeviceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+id+" not found"));
        return ResponseEntity.ok().body(device);
    }
    @RequestMapping(value="/name/{name}",method= RequestMethod.GET)
    public ResponseEntity<Device> getDevicebyName(@PathVariable String name) throws ResourceNotFoundException {
        Device device = deviceRepository.findDeviceByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+name+" not found"));
        return ResponseEntity.ok().body(device);
    }
    
    @PostMapping("/")
    public Device createDevice(@Valid @RequestBody Device device ){
        return deviceRepository.save(device);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable(value="id") int deviceId,@Valid @RequestBody Device deviceDetails)
        throws ResourceNotFoundException{
        Device device = deviceRepository.findDeviceById(deviceId)
                .orElseThrow(()->new ResourceNotFoundException("Device "+deviceId+" not found"));
        device.setName(deviceDetails.getName());
        /*device.setStatus(deviceDetails.getStatus());
        device.setType(deviceDetails.getType());*/
        final Device f_device = deviceRepository.save(device);
        return ResponseEntity.ok(f_device);
    }
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteDevice(@PathVariable(value = "id") int deviceId)
         throws ResourceNotFoundException {
        Device device = deviceRepository.findById(deviceId)
       .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id :: " + deviceId));

    @CrossOrigin
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

