/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mysmArthome.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import ua.mysmArthome.model.User;
import ua.mysmArthome.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    
    //i think the best way to find the users are username
    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @RequestMapping(value="/usr/{username}",method= RequestMethod.GET)
    public ResponseEntity<User> getUserbyUsername(@PathVariable String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));
        return ResponseEntity.ok().body(user);
    }
    @RequestMapping(value="/email/{email}",method= RequestMethod.GET)
    public ResponseEntity<User> getDevicebyName(@PathVariable String email) throws ResourceNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email "+email+" not found"));
        return ResponseEntity.ok().body(user);
    }
    
    @PostMapping("/")
    public User createUser(@Valid @RequestBody User user ){
        return userRepository.save(user);
    }
    
    @PutMapping("/{username}")
    public ResponseEntity<User> updateUsername(@PathVariable(value="username") String username,@Valid @RequestBody User userDetails)
        throws ResourceNotFoundException{
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User "+username+" not found"));
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setPhone(userDetails.getPhone());
        final User f_user = userRepository.save(user);
        return ResponseEntity.ok(f_user);
    }
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String username)
         throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username)
       .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @CrossOrigin
    @GetMapping("/login")
    public String getLogin(String username, String pwd) throws ResourceNotFoundException{
        //confirm if we received the username and token
        User user = userRepository.findUserByUsername(username)
       .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));

        if (user != null && user.getToken().equals(pwd) ){
            //if the token is from the correct user then return true
            return "{\"status\": true, \"token\":"+pwd+"}"; //send token for login
        }

        if (user!=null && user.getPassword().equals(pwd)){
            //need to generate a token for the login
            String generatedString = generateToken();
            //
            user.setToken(generatedString);
            userRepository.save(user);
            return "{\"status\": true, \"token\":"+generatedString+"}"; //send token for login
        }
        return "{\"status\":false,\"reason\":\"User and password incorrect\"}";
    }

    @CrossOrigin
    @PostMapping("/register")
    public String getRegister(String email,String username, String pwd, String confirmPwd, String phone_number) throws ResourceNotFoundException{
        User user0 = userRepository.findUserByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User "+username+" not found"));
        if (user0 != null)
            return "{\"status\":false,\"reason\":\"User already exists\"}";
        User user1 = userRepository.findUserByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User with email "+email+" not found"));
        if (user1 != null)
            return "{\"status\":false,\"reason\":\"User already exists\"}";
        if(pwd.equals(confirmPwd)){
            User user = new User(email,username,pwd,phone_number);
            //need to generate a token
            String generatedString = generateToken();
            //
            user.setToken(generatedString);
            userRepository.save(user);
            return "{\"status\":true, \"token\":"+generatedString+"}";
        }
        return "{\"status\":false,\"reason\":\"Register not successfull\"}";
    }

    private String generateToken(){
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

}
