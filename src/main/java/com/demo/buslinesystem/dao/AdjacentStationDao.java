package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.po.AdjacentStationPO;
import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/12/5 9:18 下午
 */
public interface AdjacentStationDao {

    void saveBatchInit(Collection<AdjacentStationPO> adjacentStationPOS);
}
