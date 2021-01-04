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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.Device;
import ua.mysmArthome.rabbitmq.producer.RpcProducer;
import ua.mysmArthome.repository.DeviceRepository;
import ua.mysmArthome.repository.SmartHomeRepository;

@RestController
@RequestMapping("/device")
public class DeviceController{
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private SmartHomeRepository smartHomeRepository;

    private RpcProducer producer = new RpcProducer();
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevicebyId(@PathVariable(value="id") int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+id+" not found"));
        System.out.println(device.getSmarthome().getAdmin().getUsername());
        return ResponseEntity.ok().body(device);
    }
    @RequestMapping(value="/name/{name}",method= RequestMethod.GET)
    public ResponseEntity<Device> getDevicebyName(@PathVariable String name) throws ResourceNotFoundException {
        Device device = deviceRepository.findDeviceByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+name+" not found"));
        return ResponseEntity.ok().body(device);
    }
    
    @RequestMapping(value="/shome/{id}",method= RequestMethod.GET)
    public ResponseEntity<Device> getDevicebySmartHomeId(@PathVariable int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findDevicesBySmartHomeId(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmartHome "+id+" not found"));
        return ResponseEntity.ok().body(device);
    }
    
    @PostMapping("/post/{device}/{id_home}")
    public Device createDevice(@PathVariable int id_home,@PathVariable(value="device") Device device) throws ResourceNotFoundException{
        return smartHomeRepository.findById(id_home).map(home->{
            device.setSmarthome(home);
            return deviceRepository.save(device);
        }).orElseThrow(()-> new ResourceNotFoundException("Error"));
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

        deviceRepository.delete(device);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
    @DeleteMapping("/delete")
    public void deleteAll(){
        deviceRepository.deleteAll();
    }

    @CrossOrigin
    @GetMapping("/alldevices")
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

        //return producer.createMessage("");
        
    }
    
    @CrossOrigin
    @PostMapping("/turnOn/{id}")
    public String turnOnDevice(@PathVariable(value = "id") String deviceId){
        return producer.createMessage("turnOn",deviceId);
    }

    @CrossOrigin
    @PostMapping("/turnOff/{id}")
    public String turnOffDevice(@PathVariable(value = "id") String deviceId){
        return producer.createMessage("turnOff",deviceId);
    }

    @CrossOrigin
    @GetMapping("/brightness/{id}")

    @CrossOrigin
    @GetMapping("/deviceProperty")
    public String DeviceProperty(@RequestParam(value = "property",required = true) String deviceProperty,@RequestParam(value = "id",required = true)  String deviceId){
=======
    @GetMapping("/{property}/{id}")
    public String DeviceProperty(@PathVariable(value = "property") String deviceProperty,@PathVariable(value = "id") String deviceId){
        return producer.createWithProperty("get", deviceId, deviceProperty);
    }
}
