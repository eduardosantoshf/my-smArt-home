package ua.mysmArthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.mysmArthome.repository.SmartHomeRepository;

@Controller
public class SmartHomeController {
    
    @Autowired
    private SmartHomeRepository smartHomeRepository;
}
