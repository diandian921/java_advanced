package demo.service.impl;

import annotation.Routing;
import config.enums.Datasources;
import demo.entity.User;
import demo.mapper.UserMapper;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Created by diandian
 * @date 2021/8/8.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Routing(value = Datasources.SLAVE)
    public User findUserById(int id) {
        return userMapper.findById(id);
    }

    @Override
    @Routing
    public User addUser(int id, String name) {
        userMapper.add(id, name);
        return userMapper.findById(id);
    }
}
