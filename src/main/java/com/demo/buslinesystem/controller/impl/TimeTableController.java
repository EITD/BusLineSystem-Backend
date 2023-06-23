package com.demo.buslinesystem.controller.impl;

import com.demo.buslinesystem.bean.dto.LatestDTO;
import com.demo.buslinesystem.controller.TimeTableApi;
import com.demo.buslinesystem.service.TimeTableService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TimeTableController implements TimeTableApi {

    @Resource
    TimeTableService timeTableService;

    @Override
    public List<LatestDTO> getLatest(String time, String id){
        return timeTableService.getLatest(time,id);
    }
}
