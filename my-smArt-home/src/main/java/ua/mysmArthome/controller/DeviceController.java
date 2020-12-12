package ua.mysmArthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.mysmArthome.model.Device;
import ua.mysmArthome.repository.DeviceRepository;

import java.util.List;

@Controller
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/devices")
    public void getDevices(Model model){
        List<Device> devices = deviceRepository.findAll();
        //get all devices
        model.addAttribute("devices", devices);
        /*<tbody>
            <tr th:each="device: ${devices}">
                <td th:text="${device.id}" />
                <td th:text="${device.name}" />
                <td th:text="${device.status}" />
            </tr>
        </tbody>*/ 
    }
    
}
