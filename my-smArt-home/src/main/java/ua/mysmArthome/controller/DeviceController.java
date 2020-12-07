package ua.mysmArthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ua.mysmArthome.repository.DeviceRepository;

@Controller
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    
    
}
