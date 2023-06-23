package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.po.AdjacentStationPO;
import com.demo.buslinesystem.dao.AdjacentStationDao;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/12/5 9:19 下午
 */
@Slf4j
@Repository
public class AdjacentStationDaoImpl implements AdjacentStationDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION = "adjacent_station";
    @Override
    public void saveBatchInit(Collection<AdjacentStationPO> adjacentStationPOS) {
        mongoTemplate.dropCollection(COLLECTION);
        log.info("正在批量插入adjacent_station中，size = {}", adjacentStationPOS.size());
        mongoTemplate.insertAll(adjacentStationPOS);
    }
}
