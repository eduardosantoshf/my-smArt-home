package ua.mysmArthome.controller;

import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.Device;
import ua.mysmArthome.model.SmartHome;
import ua.mysmArthome.model.User;
import ua.mysmArthome.rabbitmq.producer.RpcProducer;
import ua.mysmArthome.repository.DeviceRepository;
import ua.mysmArthome.repository.SmartHomeRepository;
import ua.mysmArthome.repository.UserRepository;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private SmartHomeRepository smartHomeRepository;

    @Autowired
    private UserRepository userRepository;

    private RpcProducer producer = new RpcProducer();
    
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevicebyId(@PathVariable(value="id") int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+id+" not found"));
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
    
    @PostMapping("/post/{id_home}")
    public Device createDevice(@PathVariable int id_home,@Valid @RequestBody Device device) throws ResourceNotFoundException{
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
    
    @DeleteMapping("/deleteAll")
    public void deleteAll(){
        deviceRepository.deleteAll();
    }

    @CrossOrigin
    @GetMapping("/alldevices/{username}")
    public String getDevices(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        User activeUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

        // get user homes

        List<SmartHome> user_homes = new ArrayList<>();
        for(Integer id : activeUser.getHomes_id()){
            user_homes.add(smartHomeRepository.findHomeById(id).orElseThrow(() -> new ResourceNotFoundException("Home " + id + " not found")));
        }

        //get all devices
        List<Device> devices = new ArrayList<>();
        for(SmartHome sm : user_homes){
            for(Device d : sm.getList_devices()){
                devices.add(d);
            }
        }
        //create appropriate string for the devices
        String devicesStr="";
        Integer counter=0;
        for (Device d : devices){
            counter++;
            devicesStr+="{\"id\":\""+d.getId()+"\",\"name\":\""+d.getName()+"\"}";
            if(counter< devices.size()){
                devicesStr+=",";
            }
        }
        //
        return "{\"devices\":["+devicesStr+"]}";

        //return producer.createMessage("");
        
    }
    
    @CrossOrigin
    @PostMapping("/turnOn")
    public String turnOnDevice(@RequestParam(value = "id",required = true) String deviceId){
        return producer.createMessage("turnOn",deviceId);
    }

    @CrossOrigin
    @PostMapping("/turnOff")
    public String turnOffDevice(@RequestParam(value = "id",required = true) String deviceId){
        return producer.createMessage("turnOff",deviceId);
    }

    @CrossOrigin
    @GetMapping("/brightness")
    public String BrightnessOfDevice(@RequestParam(value = "id",required = true)  String deviceId){
        return producer.createMessage("brightness",deviceId); //right now brightness is random
    }

    @CrossOrigin
    @GetMapping("/deviceProperty")
    public String DeviceProperty(@RequestParam(value = "property",required = true) String deviceProperty,@RequestParam(value = "id",required = true)  String deviceId){
        return producer.createWithProperty("get", deviceId, deviceProperty);
    }

    @CrossOrigin
    @GetMapping("/hardcheck/{username}")
    public String hardcheck(@PathVariable(value = "username") String username){
        String retorno = producer.createMessage("hardcheck", "");
        System.out.println(retorno);
        return "ola";
    }
}
