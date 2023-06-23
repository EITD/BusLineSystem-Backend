package com.demo.buslinesystem.controller;

import com.demo.buslinesystem.bean.dto.StationDTO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.StationPO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface StationApi {

    @GetMapping("/station/getall")
    PageBean<RoutePO> getRouteAll(@RequestParam("pagenum") int pagenum, @RequestParam("pagesize") int pagesize);

    @GetMapping("/station/get")
    List<StationDTO> getRoute(@RequestParam("name") String name);

    @GetMapping("/station/getalongStation")
    RoutePO getalongStation(@RequestParam("name") String name);

    @GetMapping("/station/alongway")
    List<LineDTO> getAlongway(@RequestParam("line") String line,
                              @RequestParam("station1") String station1,
                              @RequestParam("station2") String station2);

    @GetMapping("/station/createShort")
    void createShort();

    @GetMapping("/station/findShort")
    List<StationPO> findShort(@RequestParam("id1") String id1,@RequestParam("id2") String id2);
}
