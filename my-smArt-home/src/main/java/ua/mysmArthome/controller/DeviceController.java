package ua.mysmArthome.controller;

import java.util.*;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.*;
import ua.mysmArthome.rabbitmq.producer.RpcProducer;
import ua.mysmArthome.repository.DeviceRepository;
import ua.mysmArthome.repository.LogsRepository;
import ua.mysmArthome.repository.SmartHomeRepository;
import ua.mysmArthome.repository.UserRepository;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private SmartHomeRepository smartHomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogsRepository logsRepository;

    private RpcProducer producer = new RpcProducer();

    @GetMapping("/logs/{id}")
    public String getLogsbyId(@PathVariable(value="id") int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findDeviceByInBrokerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+id+" not found"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String retorno = "{\"logs\":\"";
        List<Log> logs = device.getLogs();
        for(Log l : logs){
            retorno+="<p>[LOG AT "+dtf.format(l.getData())+"] "+l.getValue()+"</p>";
        }
        retorno += "\"}";
        return retorno;
    }

    @GetMapping("/{id}")
    public String getDevicebyId(@PathVariable(value="id") int id) throws ResourceNotFoundException {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device "+id+" not found"));

        int borker_id = device.getInBroker_id();
        String retorno = "{\"device\":[";
        String idd="{\"id\":\""+borker_id+"\"}";
        String status = producer.createWithProperty("get", String.valueOf(borker_id), "status");
        String type = producer.createWithProperty("get", String.valueOf(borker_id), "type");
        String active = producer.createWithProperty("get", String.valueOf(borker_id), "active_since");

        retorno+=idd + "," + status + "," + type + "," + active + "]}";
        System.out.println(retorno);
        return retorno;
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
    
    @PostMapping("/post")
    public Device createDevice(Integer id_home, String device_id) throws ResourceNotFoundException{
        SmartHome sm = smartHomeRepository.findById(id_home).orElseThrow(()-> new ResourceNotFoundException("Error"));
        Device d = new Device();
        Integer id=Integer.parseInt(device_id);
        d.setInBroker_id(id);
        d.setName("");
        d.setSmarthome(sm);
        d.setList_notifications(new ArrayList<>());
        d.setLogs(new ArrayList<>());
        deviceRepository.save(d);

        Log l = new Log();
        l.setDevice(d);
        l.setValue("<p>[LOG AT "+getCurrentTime()+"] Device Found!</p>");
        l.setData(LocalDateTime.now());
        logsRepository.save(l);

        d.addListLogs(l);

        deviceRepository.save(d);
        List<Device> home_devices = sm.getList_devices();
        home_devices.add(d);
        sm.setList_devices(home_devices);

        smartHomeRepository.save(sm);
        return d;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable(value="id") int deviceId,@Valid @RequestBody Device deviceDetails)
        throws ResourceNotFoundException{
        Device device = deviceRepository.findDeviceById(deviceId)
                .orElseThrow(()->new ResourceNotFoundException("Device "+deviceId+" not found"));
        device.setName(deviceDetails.getName());
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
    public String turnOnDevice(@RequestParam(value = "id",required = true) String deviceId) throws ResourceNotFoundException {
        Integer id = Integer.valueOf(deviceId);
        Device d = deviceRepository.findDeviceByInBrokerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id :: " + deviceId));

        Log l = new Log();
        l.setDevice(d);
        l.setValue("status:turned-on");
        l.setData(LocalDateTime.now());
        logsRepository.save(l);
        d.addListLogs(l);

        deviceRepository.save(d);

        return producer.createMessage("turnOn",deviceId);

    }

    @CrossOrigin
    @PostMapping("/turnOff")
    public String turnOffDevice(@RequestParam(value = "id",required = true) String deviceId) throws ResourceNotFoundException {
        Integer id = Integer.valueOf(deviceId);
        Device d = deviceRepository.findDeviceByInBrokerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id :: " + deviceId));

        Log l = new Log();
        l.setDevice(d);
        l.setValue("status:turned-off");
        l.setData(LocalDateTime.now());
        logsRepository.save(l);
        d.addListLogs(l);

        deviceRepository.save(d);

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
    public String hardcheck(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        String id="";
        Integer home_id = userRepository.findHomesByUsername(username).getHomes_id().get(0);
        id=String.valueOf(home_id);

        // clean previous devices
        SmartHome sm = smartHomeRepository.findHomeById(home_id).orElseThrow(() -> new ResourceNotFoundException("Home " + home_id + " not found"));

        for(Device d : sm.getList_devices()){
            deviceRepository.delete(d);
        }

        String retorno = producer.createMessage("hardcheck", id);

        return retorno;
    }

    public String getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @CrossOrigin
    @GetMapping("/info/{id}")
    public String getDeviceInfo(@PathVariable(value = "id") String id) throws ResourceNotFoundException {
        String retorno="";

        Integer deviceId=Integer.valueOf(id);
        Device device = deviceRepository.findDeviceByInBrokerId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id : " + deviceId));

        String d_type = producer.createWithProperty("get", id, "type");
        JSONObject obj = new JSONObject(d_type);
        d_type = obj.getString("type");

        String curr_value = device.getLogs().get(device.getLogs().size()-1).getValue();

        String d_status = producer.createWithProperty("get", id, "status");
        obj = new JSONObject(d_status);
        d_status = obj.getString("status");

        String d_act = producer.createWithProperty("get", id, "active_since");
        obj = new JSONObject(d_act);
        d_act = obj.getString("active_since");

        List<Log> d_logs = device.getLogs();
        List<Log> d_logs_temp =new ArrayList<>();
        for(Log l : d_logs)
            if(!l.getValue().contains("Device") && !l.getValue().contains("status"))
                d_logs_temp.add(l);

        Collections.reverse(d_logs_temp);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        //type, current_value, status, active_since, logs
        retorno += "{\"type\": \""+d_type+"\", \"current_value\": \""+curr_value+"\", \"status\": \""+d_status+"\", \"active_since\": \""+d_act+"\", \"logs\":\"";

        for(Log l : d_logs_temp){
            retorno+="<p>[LOG AT "+dtf.format(l.getData())+"] "+l.getValue()+"</p>";
        }
        retorno += "\"}";

        return retorno;
    }

    @CrossOrigin
    @GetMapping("/graphs/{id}")
    public String getDeviceGraphs(@PathVariable(value = "id") String id) throws ResourceNotFoundException {
        String retorno="";

        Integer deviceId=Integer.valueOf(id);
        Device device = deviceRepository.findDeviceByInBrokerId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id : " + deviceId));


        List<Log> d_logs = device.getLogs();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        retorno += "{\"logs\":[";

        int count = 0;
        int expected_length = d_logs.size();
        for(Log l : d_logs){
            if(l.getValue().contains("status") || l.getValue().contains("Device Found")){
                expected_length--;
                continue;
            }
            count++;

            retorno+="{\"data\":\""+dtf.format(l.getData())+"\", \"value\":"+l.getValue()+"}";
            if(count<expected_length )
                retorno+=",";
        }
        retorno += "]}";

        return retorno;
    }
}
