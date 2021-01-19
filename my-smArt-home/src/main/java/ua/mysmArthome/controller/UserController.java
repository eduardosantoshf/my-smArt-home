/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.util.List;
import java.util.Random;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.mysmArthome.exception.ResourceNotFoundException;
import ua.mysmArthome.model.SmartHome;
import ua.mysmArthome.model.User;
import ua.mysmArthome.repository.SmartHomeRepository;
import ua.mysmArthome.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartHomeRepository smarthomeRepository;

    @PostMapping("/post/{confirm}")
    public void createUser(@PathVariable String confirm, @Valid @RequestBody User user) throws ResourceNotFoundException {
        System.out.println(this.getRegister(user.getEmail(), user.getUsername(), user.getPassword(), confirm, user.getPhone()));
    }

    @CrossOrigin
    @GetMapping("/login")
    public String getLogin(String username, String pwd) throws ResourceNotFoundException {

        if (userRepository.findUserByUsername(username).isPresent()) {
            User user = userRepository.findUserByUsername(username).get();
            if (user.getToken().equals(pwd)) //if the token is from the correct user then return true
            {
                return "{\"status\": true, \"token\": \"" + pwd + "\"}"; //send token for login
            }
            if (user.getPassword().equals(pwd)) {
                //need to generate a token for the login
                String generatedString = generateToken();
                //
                user.setToken(generatedString);
                userRepository.save(user);
                return "{\"status\": true, \"token\": \"" + generatedString + "\"}"; //send token for login
            }

        }
        return "{\"status\":false,\"reason\":\"User and password incorrect\"}";
    }

    @CrossOrigin
    @PostMapping("/register")
    public String getRegister(String email, String username, String pwd, String confirmPwd, String phone_number) throws ResourceNotFoundException {
        if (userRepository.findUserByUsername(username).isPresent()) {
            return "{\"status\":false,\"reason\":\"User already exists\"}";
        }
        if (pwd.equals(confirmPwd)) {
            User user = new User(email, username, pwd, phone_number);
            //need to generate a token
            String generatedString = generateToken();
            //
            user.setToken(generatedString);
            userRepository.save(user);
            return "{\"status\":true, \"token\":\"" + generatedString + "\"}";
        }
        return "{\"status\":false,\"reason\":\"Register not successfull\"}";
    }

    private String generateToken() {
        //need to generate a token for the login
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
        //
    }

    @CrossOrigin
    @PostMapping("/smarthome")
    public void createSmarthomeUser(String username) throws ResourceNotFoundException {
        User activeUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        List<Integer> user_sm = activeUser.getHomes_id();
        SmartHome sm = new SmartHome("casa");
        smarthomeRepository.save(sm);
        user_sm.add(sm.getId());
        activeUser.setHomes_id(user_sm);
        userRepository.save(activeUser);
    }

    @CrossOrigin
    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable(value = "id") String id) throws ResourceNotFoundException {
        //for profile
        String retorno="";

        User user = userRepository.findUserByUsername(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found for this id : " + id));

        String email = user.getEmail();
        String username = user.getUsername();
        String phone = user.getPhone();

        retorno += "{\"username\":\""+username+"\",\"email\":\""+email+"\",\"phone\":\""+phone+"\"}";

        return retorno;
    }

}
