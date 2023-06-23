package com.demo.buslinesystem.service.impl;

import com.demo.buslinesystem.bean.dto.LatestDTO;
import com.demo.buslinesystem.controller.TimeTableApi;
import com.demo.buslinesystem.dao.RouteDao;
import com.demo.buslinesystem.dao.StationDao;
import com.demo.buslinesystem.dao.TimeTableDao;
import com.demo.buslinesystem.service.TimeTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TimeTableServiceImpl implements TimeTableService {
    @Resource
    StationDao stationDao;

    @Resource
    RouteDao routeDao;

    @Resource
    TimeTableDao timeTableDao;

    @Override
    public List<LatestDTO> getLatest(String time, String id){
        List<LatestDTO> latestdtos = routeDao.getLatest(id);
        List<LatestDTO> result = timeTableDao.getLatest(latestdtos,time);
        return result;
    }

    @Override
    public void add(String name,String first,String timetable,String interval,String number) throws ParseException {
        List<String> list = Arrays.asList(timetable.split(","));
        List<List<String>> result = new ArrayList<>();
        for(int i=0;i<Integer.parseInt(number);i++){
            List<String> temp = new ArrayList<>();
            if(i==0)temp.add(first);
            else if(i>0)temp.add(addTime(result.get(i-1).get(0),interval));
            for(int j=1;j<=list.size();j++){
                temp.add(addTime(temp.get(j-1),list.get(j-1)));
            }
            result.add(temp);
        }
        timeTableDao.add(name,result);
    }

    public String addTime(String str1, String str2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date1 = sdf.parse(str1);
        Date result = new Date(date1.getTime()+Integer.parseInt(str2)*60*1000);
        return sdf.format(result);
    }
}
