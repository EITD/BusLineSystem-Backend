package com.demo.buslinesystem.controller.impl;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.UserPO;
import com.demo.buslinesystem.controller.StatisticsApi;
import com.demo.buslinesystem.dao.*;
import com.demo.buslinesystem.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class StatisticsController implements StatisticsApi {

    @Resource
    private StationDao stationDao;
    @Resource
    private RouteDao routeDao;
    @Resource
    private LineDao lineDao;
    @Resource
    private TimeTableDao timeTableDao;
    @Resource
    private UserDao userDao;
    @Resource
    private LineService lineService;


    @Override
    public List<StationStatisticsDTO> stationCountSort(Integer option, Integer num) {
        List<StationStatisticsDTO> stationStatisticsDTOS = stationDao.sortById(option, num);
        return stationStatisticsDTOS;
    }

    @Override
    public List<LStaCountDTO> lCountSortBySNum(Integer option, Integer num) {
        if (num == null) {
            num = 20;
        }
        List<LStaCountDTO> LStaCountDTOS = routeDao.sortBySNum(option, num);
        return LStaCountDTOS;
    }

    @Override
    public List<LTDTO> lCountSortByT(Integer option, Integer num) {
        List<LTDTO> LTDTOS = lineDao.sortedByT(option, num);
        return LTDTOS;
    }

    @Override
    public List<String> findRepeat(String station1, String station2) {
        List<String> repeatStations = routeDao.repeatSid(station1, station2);
        return repeatStations;
    }

    @Override
    public List<String> findRepeatStation(String line1, String line2) {
        List<String> repeatStationss = routeDao.repeatStation(line1, line2);

        return repeatStationss;
    }

    @Override
    public List<Integer> busCount() {

        List<Integer> result = lineDao.lineCountByGroup();
        return result;
    }

    @Override
    public List<StationGroupDTO> stationCount() {
        List<StationGroupDTO> result = stationDao.stationCountByGroup();
        return result;
    }


    @Override
    public List<S1L1DTO> changeRoadCount(String route) {
        List<S1L1DTO> result = routeDao.cRoadCount(route);
        return result;
    }

    @Override
    public List<LStaCountDTO> changeRoadCountSort(Integer option, Integer num) {
        if (option == null) {
            option = 0;
        }
        if (num == null) {
            num = 20;
        }
        List<LStaCountDTO> LStaCountDTOS = routeDao.changeSort(option, num);
        return LStaCountDTOS;
    }

    @Override
    public ResponseEntity<String> createAllTwoStationChange() {
        stationDao.createAllTwoStationChange();
        return ResponseEntity.ok("插入成功");
    }

    @Override
    public ResponseEntity<String> createAdjacentStationChange() {
        stationDao.createAdjacentStationChange();
        return ResponseEntity.ok("插入成功");
    }

    @Override
    public List<AdjacentStationDTO> queryAdjacentStationChangeLine(Integer option, Integer num) {
        if (num == null) {
            num = 20;
        }
        long begin = System.currentTimeMillis();
        List<AdjacentStationDTO> adjacentStationDTOS = stationDao.queryAdjacentStationChangeLine(option, num);
        long end = System.currentTimeMillis();
        log.info("查询用时: {} ms", end - begin);
        return adjacentStationDTOS;
    }

//    @Override
//    public List<UserPO> queryAdjacentStationChangeLine2(Integer option, Integer num) {
//        List<UserPO> result = routeDao.TwoStationLineSort(option,num);
//        return result;
//    }

    @Override
    public Integer nonRepeatLine(String line1, String line2) {
//        Integer result = userDao.nonRepeatCo(line1,)
        return 0;
    }

    @Override
    public List<LineTimeDTO> routeSortByRuntime(Integer option, Integer num) {
        List<LineTimeDTO> result = timeTableDao.rSortByRuntime(option, num);
        return result;
    }

    /**
     * @param line
     * @return
     */
    @Override
    public Double nonRepeat(String line) {
        Double result = userDao.nonRepeatCo(line);

        return result;
    }

    @Override
    public Object[] findLines() {
        Object[] result = lineDao.selectLines();
        return result;
    }

    @Override
    public Object[] findRoutes() {
        Object[] result = routeDao.selectLines();
        return result;
    }

    @Override
    public Object[] findStations() {
        Object[] result = stationDao.selectStations();
        return result;
    }

    @Override
    public Object[] findStationsById() {
        Object[] result = stationDao.selectStationsById();
        return result;
    }

    @Override
    public List<String> directStation(String line) {
        List<String> result = lineService.directStationCount(line);
        return result;
    }

    @Override
    public List<String> alongStationCh(String line) {

        List<String> result = lineService.stationtoChName(line);
        return result;

    }


}


