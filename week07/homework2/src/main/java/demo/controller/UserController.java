package demo.controller;

import demo.entity.User;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by diandian
 * @date 2021/8/8.
 */
@RestController
@RequestMapping("/route")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    public User findUserById(int id) {
        return userService.findUserById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public User add(int id, String name) {
       return userService.addUser(id, name);
    }

}
