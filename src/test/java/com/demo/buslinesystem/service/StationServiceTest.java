package com.demo.buslinesystem.service;

import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.dao.RouteDao;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/10/15 10:07 上午
 */
@Slf4j
@SpringBootTest
class StationServiceTest {

    @Resource
    private StationService stationService;

    @Test
    void getRouteAll() {
        PageBean<RoutePO> pb = stationService.getRouteAll(1,5);
        log.info(pb.toString());
    }

}
