package com.demo.buslinesystem.controller;

import com.demo.buslinesystem.bean.dto.*;

import java.util.List;

import com.demo.buslinesystem.bean.po.UserPO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface StatisticsApi {


    @GetMapping("/statistics/stationSortById")
    @CrossOrigin
    List<StationStatisticsDTO> stationCountSort(@RequestParam("option") Integer option,
                                                @RequestParam("num") Integer num);

    @GetMapping("/statistics/lineSortByStaNum")
    @CrossOrigin
    List<LStaCountDTO> lCountSortBySNum(@RequestParam("option") Integer option,
                                        @RequestParam("num") Integer num);


    @GetMapping("/statistics/lineSortByTime")
    @CrossOrigin
    List<LTDTO> lCountSortByT(@RequestParam("option") Integer option,
                              @RequestParam("num") Integer num);

    @GetMapping("/statistics/repeatLine")
    @CrossOrigin
    List<String> findRepeat(@RequestParam("station1") String station1,
                            @RequestParam("station2") String station2);

    @GetMapping("/statistics/repeatStation")
    @CrossOrigin
    List<String> findRepeatStation(@RequestParam("line1") String line1,
                                   @RequestParam("line2") String line2);

    @GetMapping("/statistics/busCount")
    @CrossOrigin
    List<Integer> busCount();

    @GetMapping("/statistics/stationCount")
    @CrossOrigin
    List<StationGroupDTO> stationCount();

    @GetMapping("/statistics/changeRoad")
    @CrossOrigin
    List<S1L1DTO> changeRoadCount(@RequestParam("route") String route);

    /**
     * TODO
     *
     * @param option
     * @param num
     * @return
     */
    @GetMapping("/statistics/changeRoadSort")
    @CrossOrigin
    List<LStaCountDTO> changeRoadCountSort(@RequestParam("option") Integer option,
                                           @RequestParam("num") Integer num);


    /**
     *
     * @return
     */
    @PutMapping("/statistics/twoStationChange/save")
    ResponseEntity<String> createAllTwoStationChange();

    /**
     *
     * @return
     */
    @PutMapping("/statistics/adjacentStationChange/save")
    ResponseEntity<String> createAdjacentStationChange();

    @GetMapping("/statistics/straightRoadSort")
    @CrossOrigin
    List<AdjacentStationDTO> queryAdjacentStationChangeLine(@RequestParam("option") Integer option,
                                                @RequestParam("num") Integer num);
//
//    @GetMapping("/statistics/straightRoadSort2")
//    @CrossOrigin
//    List<UserPO> queryAdjacentStationChangeLine2(@RequestParam("option") Integer option,
//                                                            @RequestParam("num") Integer num);

    @GetMapping("/statistics/nonRepeatLine")
    @CrossOrigin
    Integer nonRepeatLine(@RequestParam("line1") String line1,
                          @RequestParam("line2") String line2);

    @GetMapping("/statistics/routeSortByRuntime")
    @CrossOrigin
    List<LineTimeDTO> routeSortByRuntime(@RequestParam("option") Integer option,
                                         @RequestParam("num") Integer num);

    @GetMapping("/statistics/nonRepeat")
    @CrossOrigin
    Double nonRepeat(@RequestParam("line") String line );

    @GetMapping("/statistics/findLines")
    @CrossOrigin
    Object[] findLines();

    @GetMapping("/statistics/findRoutes")
    @CrossOrigin
    Object[] findRoutes();

    @GetMapping("/statistics/findStations")
    @CrossOrigin
    Object[] findStations();

    @GetMapping("/statistics/findStationsById")
    @CrossOrigin
    Object[] findStationsById();

    @GetMapping("/statistics/directStation")
    @CrossOrigin
    List<String> directStation(@RequestParam("line") String line);

    @GetMapping("/statistics/alongStationCh")
    @CrossOrigin
    List<String> alongStationCh(@RequestParam("line") String line);



}

