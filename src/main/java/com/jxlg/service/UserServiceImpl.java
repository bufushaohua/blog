package com.jxlg.service;

import com.jxlg.NotFoundException;
import com.jxlg.dao.UserRepository;
import com.jxlg.po.User;
import com.jxlg.util.MD5Utils;
import com.jxlg.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    使用MD5Utils加密密码，更加安全
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public User updateUser(Long id, User user) {
        User u = userRepository.findById(id).get();
        if (u == null) {
            throw new NotFoundException("该用户不存在");
        }
        if(!user.getPassword().equals(u.getPassword())){
            user.setPassword(MD5Utils.code(user.getPassword()));
        }
        BeanUtils.copyProperties(user,u);  // 将user中的属性值copy到u中
        return userRepository.save(u);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        //保存的时候判断是新增Blog还是修改Blog
        if(user.getId() == null){  //是新增
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setPassword(MD5Utils.code(user.getPassword()));
        }else{
            user.setUpdateTime(new Date()); //不需要重新创建时间，直接更新时间就行
        }
        return userRepository.save(user);
    }

}
