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
import ua.mysmArthome.model.Admin;
import ua.mysmArthome.repository.AdminRepository;
import ua.mysmArthome.repository.UserRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepostiory;
    //i think the best way to find the users are username

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserbyUsername(@PathVariable String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) throws ResourceNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.GET)
    public List<User> getUserByAdminId(@PathVariable int id) throws ResourceNotFoundException {
        if (adminRepostiory.findAdminById(id).isEmpty()) {
            throw new ResourceNotFoundException("Not Found");
        }
        return userRepository.findUserByAdminId(id);
    }

    @PostMapping("/post/{id_admin}/user")
    public void createUser(@PathVariable int id_admin, @Valid @RequestBody User user) throws ResourceNotFoundException {
        System.out.println(this.getRegister(user.getEmail(), user.getUsername(), user.getPassword(), "password", user.getPhone()));
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "username") String username, @Valid @RequestBody User userDetails)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setPhone(userDetails.getPhone());
        final User f_user = userRepository.save(user);
        return ResponseEntity.ok(f_user);
    }

    @DeleteMapping("/{username}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/delete")
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @CrossOrigin
    @GetMapping("/login")
    public String getLogin(String username, String pwd) throws ResourceNotFoundException {

        if (userRepository.findUserByUsername(username).isPresent()) {
            User user = userRepository.findUserByUsername(username).get();
            if (user.getToken().equals(pwd)) //if the token is from the correct user then return true
            {
                return "{\"status\": true, \"token\":" + pwd + "}"; //send token for login
            }
            if (user.getPassword().equals(pwd)) {
                //need to generate a token for the login
                String generatedString = generateToken();
                //
                user.setToken(generatedString);
                userRepository.save(user);
                return "{\"status\": true, \"token\":" + generatedString + "}"; //send token for login
            }

        }
        return "{\"status\":false,\"reason\":\"User and password incorrect\"}";
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/register")
    public String getRegister(String email, String username, String pwd, String confirmPwd, String phone_number) throws ResourceNotFoundException {

        if (userRepository.findUserByUsername(username).isPresent()) {
            return "{\"status\":false,\"reason\":\"User already exists\"}";
        }
        if (userRepository.findUserByEmail(email).isPresent()) {
            return "{\"status\":false,\"reason\":\"User already exists\"}";
        }
        Admin admin =  new Admin(0, "admin", "admin@ies.com", "password", "123456789");
        if (pwd.equals(confirmPwd)) {
            User user = new User(email, username, pwd, phone_number);
            user.setAdmin(admin);
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

}
