package com.demo.buslinesystem.controller;

import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.dto.TimeTableDTO;
import com.demo.buslinesystem.bean.po.LinePO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.TimeTablePO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface LineApi {

    @GetMapping("/line/get")
    List<LinePO> getLine(@RequestParam("name") String name);

    @GetMapping("/line/list")
    List<RoutePO> getLineList(@RequestParam("station1") String station1,
                              @RequestParam("station2") String station2);

    @GetMapping("/line/stop")
    List<Map<String,List<String>>> getLineStop(@RequestParam("name") String name);

    @GetMapping("/line/timetable")
    List<List<String>> getTimeTable(@RequestParam("name") String name);
    @GetMapping("/line/station")
    List<String> getStation(@RequestParam("name") String name);

    @GetMapping("/line/add")
    void add(@RequestParam("name") String name,@RequestParam("alongStation") String alongStation,@RequestParam("first") String first,@RequestParam("timetable") String timetable,@RequestParam("interval") String interval,@RequestParam("number") String number) throws ParseException;

    @GetMapping("/line/edit")
    void edit(@RequestParam("name") String name,@RequestParam("alongStation") String alongStation);

    @GetMapping("/line/delete")
    void delete(@RequestParam("name") String name);

    @GetMapping("/line/stopline")
    List<StopLineDTO> getStopLine(@RequestParam("time") String time,@RequestParam("id") String id,@RequestParam("minute") String minute) throws ParseException;
}
