package com.demo.buslinesystem.controller.impl;

import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.dto.TimeTableDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.TimeTablePO;
import com.demo.buslinesystem.controller.LineApi;
import com.demo.buslinesystem.dao.RouteDao;
import com.demo.buslinesystem.dao.StationDao;
import com.demo.buslinesystem.service.LineService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.demo.buslinesystem.service.TimeTableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LineController implements LineApi {

    @Resource
    private LineService lineService;
    @Resource
    private TimeTableService timetableService;

    @Resource
    private StationDao stationDao;
    @Resource
    private RouteDao routeDao;

    @Override
    public List<LinePO> getLine(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return lineService.getLineInfo(name);
    }

    @Override
    public List<RoutePO> getLineList(String station1, String station2) {
        List<String> stationIds1 = stationDao.getIdByName(station1);
        List<String> stationIds2 = stationDao.getIdByName(station2);
        List<RoutePO> routePOList = routeDao.queryRoutesExist(stationIds1, stationIds2);
//        List<String> nameList = routePOList.stream().map(RoutePO::getName).collect(Collectors.toList());
        return routePOList;
    }

    @Override
    public List<Map<String,List<String>>> getLineStop(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return lineService.getLineStop(name);
    }

    @Override
    public List<List<String>> getTimeTable(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return lineService.getTimeTable(name);
    }
    @Override
    public List<String> getStation(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return lineService.getStation(name);
    }

    @Override
    public void add(String name,String alongStation,String first,String timetable,String interval,String number) throws ParseException {
        lineService.add(name,alongStation);
        timetableService.add(name,first,timetable,interval,number);
    }

    @Override
    public void edit(String name,String alongStation){
        lineService.edit(name,alongStation);
    }

    @Override
    public void delete(String name){
        lineService.delete(name);
    }

    @Override
    public List<StopLineDTO> getStopLine(String time, String id, String minute) throws ParseException {
        return lineService.getStopLine(time,id,minute);
    }

}
