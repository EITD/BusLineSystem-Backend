package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.LatestDTO;
import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.po.RoutePO;

import com.demo.buslinesystem.bean.dto.LineDTO;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/10/15 10:07 上午
 */
@Slf4j
@SpringBootTest
class RouteDaoTest {

    @Resource
    private RouteDao routeDao;

    @Test
    void queryRoutesExist() {
        List<RoutePO> routePOS = routeDao.queryRoutesExist(Lists.list("64838", "64839", "27145", "27134"),
            Lists.list("114519", "3539", "15343"));
        log.info(routePOS.toString());
    }

    @Test
    void getLineStop(){
        List<Map<String,List<String>>> map = routeDao.getLineStop(Lists.list("64355", "64356", "58289", "58290"));
        log.info(map.toString());
    }

    @Test
    void findTotal(){
        int i = routeDao.findTotal();
        log.info(String.valueOf(i));
    }

    @Test
    void getRouteByPage(){
        List<RoutePO> routePOS = routeDao.getRouteByPage(0,5);
        log.info(routePOS.toString());
    }

    @Test
    void getRoute(){
        List<RoutePO> routePOS = routeDao.getRoute("2路");
        log.info(routePOS.toString());
    }

    @Test
    void getAlongway(){
        List<LineDTO> list = routeDao.getAlongway("10路",Lists.list("62752", "62753"),Lists.list("6377", "6378"));
        log.info(list.toString());
    }

//    @Test
//    void getStopLine(){
//        List<StopLineDTO> list = routeDao.getStopLine(Lists.list("16147","16148"));
//        log.info(list.toString());
//    }

//    @Test
//    void getLatest(){
//        List<LatestDTO> list = routeDao.getLatest(Lists.list("59760","59761"),"82路");
//        log.info(list.toString());
//    }
}
