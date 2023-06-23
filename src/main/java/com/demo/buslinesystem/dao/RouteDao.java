package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.UserPO;

import java.util.List;
import java.util.Map;


public interface RouteDao {

    List<LStaCountDTO> sortBySNum(Integer option, Integer num);

    int findTotal();

    //List<UserPO> TwoStationLineSort(Integer option, Integer num);

    List<RoutePO> getRouteByPage(int start, int pagesize);

    List<RoutePO> queryRoutesExist(List<String> ids1, List<String> ids2);

    List<String> repeatSid(String station1, String station2);

    List<String> repeatStation(String line1, String line2);

    List<S1L1DTO> cRoadCount(String route);

    List<LStaCountDTO> changeSort(Integer option, Integer num);

    Integer straightRoadCount(String station1, String station2);

    List<Map<String, List<String>>> getLineStop(List<String> ids);

    List<RoutePO> getRoute(String name);

    RoutePO queryRoute(String name);

    List<LineDTO> getAlongway(String line, List<String> ids1, List<String> ids2);

    void add(String name, List<String> alongStation);

    void edit(String name, List<String> alongStation);

    void delete(String name);

    RoutePO getalongStation(String name);

    List<StopLineDTO> getStopLine(String id);

    List<LatestDTO> getLatest(String id);

    /**
     * @param routePOS
     */
    void batchUpdateRoute(List<RoutePO> routePOS);

    Object[] selectLines();
}
