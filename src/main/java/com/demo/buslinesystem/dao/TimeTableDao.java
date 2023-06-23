package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.TimeTablePO;

import java.text.ParseException;
import java.util.List;

public interface TimeTableDao {
    List<LineTimeDTO> rSortByRuntime(Integer option, Integer num) ;

    List<List<String>> getTimeTable(String name);

    List<StopLineDTO> getStopLine(List<StopLineDTO> stoplinedtos, List<String> times);

    List<LatestDTO> getLatest(List<LatestDTO> latestdtos,String time);

    List<LineDTO> getTime(List<LineDTO> route);

    void add(String name,List<List<String>> timetable);

//    List<TimeTablePO>test(StopLineDTO stoplinedto, List<String> times);
//    int getTime(String str1,String str2) throws ParseException;
}
