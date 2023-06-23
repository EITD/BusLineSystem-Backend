package com.demo.buslinesystem.service;

import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.dto.StationDTO;
import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.StationPO;

import java.util.List;

public interface StationService {

    PageBean<RoutePO> getRouteAll(int pagenum, int pagesize);

    List<StationDTO> getRoute(String name);

    List<LineDTO> getAlongway(String line, String station1, String station2);

    RoutePO getalongStation(String name);

    void createShort();

    List<StationPO> findShort(String id1, String id2);
}
