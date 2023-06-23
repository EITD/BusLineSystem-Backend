package com.demo.buslinesystem.service;

import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.dto.TimeTableDTO;
import com.demo.buslinesystem.bean.dto.TwoSnameWithCountDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.po.LinePO;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.demo.buslinesystem.bean.po.TimeTablePO;
import org.springframework.stereotype.Service;


public interface LineService {

    List<TwoSnameWithCountDTO> straightSort(Integer option, Integer num);

    Double nonRepeatCo(String line);

    List<LinePO> getLineInfo(String line);

    List<Map<String,List<String>>> getLineStop(String name);

    List<List<String>> getTimeTable(String name);
    List<String> getStation(String name);

    void add(String name,String alongStation);

    void edit(String name,String alongStation);

    void delete(String name);

    List<StopLineDTO> getStopLine(String time, String id, String minute) throws ParseException;
    List<String> getTimeList(String currenttime, String minute) throws ParseException;

    List<String> toChName(List<String> alongStation);

    List<String> directStationCount(String line);

    List<String>  to_line_ChName(List<String> lines);


    List<String> stationtoChName(String line);
}
