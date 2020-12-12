package ua.mysmArthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.mysmArthome.repository.UserRepository;
import ua.mysmArthome.model.User;

@Controller
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    /*Add something like this to receive values for /login and /register
    <form action="#" th:action="@{/saveStudent}" th:object="${student}" method="post">
        <table border="1">
            <tr>
                <td><label th:text="#{msg.id}" /></td>
                <td><input type="number" th:field="*{id}" /></td>
            </tr>
            <tr>
                <td><label th:text="#{msg.name}" /></td>
                <td><input type="text" th:field="*{name}" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="Submit" /></td>
            </tr>
        </table>
    </form>  */

    @GetMapping("/login")
    public boolean getLogin(String username, String pwd){
        User user = userRepository.findByUsername(username);
        if (user.getPassword().equals(pwd))
            return true;
        return false;
    }

    @GetMapping("/register")
    public boolean getRegister(String email,String username, String pwd, String confirmPwd){
        User user0 = userRepository.findByUsername(username);
        if (user0 != null)
            return false;
        User user1 = userRepository.findByEmail(email);
        if (user1 != null)
            return false;
        if(pwd.equals(confirmPwd))
            return true;
        return false;
    }
}
