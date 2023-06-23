package com.demo.buslinesystem.service.impl;

import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.dto.StationDTO;
import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.ShortPO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.dao.*;
import com.demo.buslinesystem.service.StationService;
import com.google.common.collect.Lists;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class StationServiceImpl implements StationService {

    @Resource
    private RouteDao routeDao;

    @Resource
    private StationDao stationDao;

    @Resource
    private TimeTableDao timeTableDao;

    @Resource
    private ShortDao shortDao;

    @Override
    public PageBean<RoutePO> getRouteAll(int pagenum, int pagesize) {

        PageBean<RoutePO> pb = new PageBean<RoutePO>();
        pb.setPagenum(pagenum);
        pb.setPagesize(pagesize);

        int total = routeDao.findTotal();
        pb.setTotal(total);

        int start = (pagenum - 1) * pagesize;
        List<RoutePO> route = routeDao.getRouteByPage(start, pagesize);
        List<RoutePO> list = stationDao.getNameById(route);
        pb.setList(list);

        int totalPage = (total % pagesize) == 0 ? total / pagesize : (total / pagesize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public List<StationDTO> getRoute(String name) {
        if (name == null) {
            return Lists.newArrayList();
        }
        RoutePO routePO = routeDao.queryRoute(name);
        List<StationPO> stationPOS = stationDao.queryByIds(routePO.getAlongStation());
        return stationPOS.stream()
            .map(e -> StationDTO.builder()
                .linename(routePO.getName())
                .id(e.getId())
                .stationname(e.getName())
                .english(e.getEnglish())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<LineDTO> getAlongway(String line, String station1, String station2) {
        List<String> ids1 = stationDao.getIdByName(station1);
        List<String> ids2 = stationDao.getIdByName(station2);
        List<LineDTO> route = routeDao.getAlongway(line, ids1, ids2);
//        List<LineDTO> linedto = lineDao.getTime(route);
        List<LineDTO> linedto = timeTableDao.getTime(route);
        List<LineDTO> result = stationDao.getNameById2(linedto);
        return result;
    }

    @Override
    public RoutePO getalongStation(String name) {
        return routeDao.getalongStation(name);
    }

    public void createShort() {
        List<ShortPO> shortPOS = stationDao.createShort();
        shortDao.saveBatch(shortPOS);
    }

    @Override
    public List<StationPO> findShort(String id1, String id2) {
        List<String> ids = shortDao.findShort(id1, id2);
        List<StationPO> result = stationDao.findShort(ids);
        return result;
    }

}
