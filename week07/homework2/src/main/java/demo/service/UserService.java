package demo.service;

import demo.entity.User;

/**
 * @author Created by diandian
 * @date 2021/8/8.
 */
public interface UserService {

    User findUserById(int id);

    User addUser(int id, String name);
}
