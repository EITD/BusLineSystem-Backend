package com.demo.buslinesystem.dao;


import com.demo.buslinesystem.bean.dto.nonRepeatDTO;
import com.demo.buslinesystem.bean.po.UserPO;
import org.apache.catalina.User;

import java.util.List;

public interface UserDao {

    void saveBatch(List<UserPO> userPOS);

    void updateBatch(List<UserPO> userPOS);

    void saveUser(UserPO userPO);    

    void removeUser(Integer id);    

    UserPO findUserByName(String name);    

    int updateUser(UserPO userPO);          

    Double nonRepeatCo(String line);

    List<User> insertCollection(List<User> userList) throws Exception;

}
