package com.jxlg.dao;

import com.jxlg.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username, String password);

    @Transactional
    @Modifying
    @Query("update User u set u.username = ?2, u.nickname = ?3, u.password = ?4, u.email = ?5, u.avatar = ?6 where u.id = ?1")
    int updateUser(Long id,String usename,String nickname,String password,String email,String avartar);
}
