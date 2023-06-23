package com.demo.buslinesystem.service;

import com.demo.buslinesystem.bean.dto.LatestDTO;

import java.text.ParseException;
import java.util.List;

public interface TimeTableService {
    List<LatestDTO> getLatest(String time, String id);

    void add(String name,String first,String timetable,String interval,String number) throws ParseException;
}
