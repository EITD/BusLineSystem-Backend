package com.demo.buslinesystem.controller.impl;

import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.dto.StationDTO;
import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.controller.StationApi;
import com.demo.buslinesystem.dao.RouteDao;
import com.demo.buslinesystem.dao.StationDao;
import com.demo.buslinesystem.service.StationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class StationController implements StationApi {
    @Resource
    private StationService stationService;

    @Resource
    private StationDao stationDao;
    @Resource
    private RouteDao routeDao;

    @Override
    public PageBean<RoutePO> getRouteAll(int pagenum, int pagesize) {
        return stationService.getRouteAll(pagenum,pagesize);
    }

    @Override
    public List<StationDTO> getRoute(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return stationService.getRoute(name);
    }

    @Override
    public List<LineDTO> getAlongway(String line, String station1, String station2){
        return stationService.getAlongway(line,station1,station2);
    }

    @Override
    public RoutePO getalongStation(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return stationService.getalongStation(name);
    }

    @Override
    public  void createShort(){
        stationService.createShort();
    }

    @Override
    public List<StationPO> findShort(String id1, String id2){
        return stationService.findShort(id1,id2);
    }
}
