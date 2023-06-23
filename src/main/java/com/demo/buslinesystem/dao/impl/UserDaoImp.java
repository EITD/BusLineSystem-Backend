package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.dto.AdjacentStationDTO;
import com.demo.buslinesystem.bean.po.AdjacentStationPO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.UserPO;
import com.demo.buslinesystem.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component      
public class UserDaoImp implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION = "user";

    @Override
    public void saveBatch(List<UserPO> userPOS) {
        mongoTemplate.dropCollection(COLLECTION);
        log.info("正在批量插入user中，size = {}", userPOS.size());
        mongoTemplate.insertAll(userPOS);
    }

    @Override
    public void updateBatch(List<UserPO> userPOS) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, COLLECTION);
        List<Pair<Query, Update>> updateList = new ArrayList<>();
        userPOS.forEach(po -> {
            Query query = Query.query(Criteria.where("id").is(po.getId()));
            Update update = new Update();
//            update.set("adjacent", po.getAdjacent());
//            update.set("line", po.getLine());
            updateList.add(Pair.of(query, update));
        });
        bulkOperations.updateMulti(updateList);
        bulkOperations.execute();
    }

    @Override
    public void saveUser(UserPO userPO) {
        mongoTemplate.save(userPO);
    }

    @Override
    public void removeUser(Integer id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, UserPO.class);
    }

    @Override
    public UserPO findUserByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        UserPO userPO = mongoTemplate.findOne(query, UserPO.class);
        return userPO;
    }

    @Override
    public int updateUser(UserPO userPO) {
        return 0;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnitKey {
        String id1;
        String id2;
    }

    @Override
    public Double nonRepeatCo(String line) {
        Query query = new Query(Criteria.where("name").is(line));
        RoutePO routePO = mongoTemplate.findOne(query, RoutePO.class);

        if (routePO == null) {
            return null;
        }
        List<UserPO> userPO = mongoTemplate.find(Query.query(Criteria.where("route_name").is(routePO.getName())),UserPO.class);


        double sum = 0.0;
        int count = 0;

        for (UserPO po : userPO) {
            int a = po.getNum();
            sum +=  (1.0 / (a+1));
            count++;

        }
        sum = sum / count;
        return sum;

    }

    @Override
    public List<User> insertCollection(List<User> userList) throws Exception {
        return (List<User>) mongoTemplate.insert(userList, User.class);
    }

//    @Override
//    public int updateUser(User user) {
//        Query query = new Query(Criteria.where("id").is(user.getStation1()));
//        Update update = new Update().set("name",user.getName()).set("password",user.getPassword());
//        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
//        //UpdateResult all_result = mongoTemplate.updateMulti(query,update,User.class);
//        if(result!= null)
//            return (int) result.getModifiedCount();
//        else
//            return 0;
//    }
}