package com.demo.buslinesystem.controller;

import com.demo.buslinesystem.bean.dto.LatestDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface TimeTableApi {
    @GetMapping("/timetable/latest")
    List<LatestDTO> getLatest(@RequestParam("time") String time,@RequestParam("id") String id);
}
