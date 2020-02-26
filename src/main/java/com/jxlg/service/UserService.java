package com.jxlg.service;


import com.jxlg.po.User;

public interface UserService {

    User checkUser(String username,String password);

    User getUser(Long id);

    User updateUser(Long id, User user);

    User saveUser(User u);
}
