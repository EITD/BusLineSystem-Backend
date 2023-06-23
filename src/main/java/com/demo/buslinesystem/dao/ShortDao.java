package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.po.ShortPO;
import com.demo.buslinesystem.bean.po.UserPO;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

public interface ShortDao {
    void saveBatch(List<ShortPO> shortPOS);

    List<String> findShort(String id1,String id2);
}
