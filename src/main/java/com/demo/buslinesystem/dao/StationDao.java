package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.AdjacentStationPO;
import com.demo.buslinesystem.bean.po.RoutePO;

import com.demo.buslinesystem.bean.po.ShortPO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.bean.po.UserPO;

import java.util.List;
import java.util.Set;


public interface StationDao {

    List<StationPO> queryByIds(List<String> stationIds);

    /**
     * @param station
     * @return
     */
    List<String> getIdByName(String station);

    List<RoutePO> getNameById(List<RoutePO> list);

    List<StationDTO> getStation(List<RoutePO> route);

    List<LineDTO> getNameById2(List<LineDTO> list);

    void delete(String id);

    List<ShortPO> createShort();

    List<StationPO> findShort(List<String> ids);

    List<StationStatisticsDTO> sortById(Integer option, Integer num);

    String getNameById(String station);

    List<StationGroupDTO> stationCountByGroup();

    List<UserPO> straightSort (Integer option, Integer num);

    List<UserPO> straightSort2(Integer option, Integer num);


    List<UserPO> twoStationLine(Integer option, Integer num);

    Object[] selectStations();

    Object[] selectStationsById();

    Set<UserPO> createAllTwoStationChange();

    Set<AdjacentStationPO> createAdjacentStationChange();

    List<AdjacentStationDTO> queryAdjacentStationChangeLine(Integer option, Integer num);
}
