package com.demo.buslinesystem.service.impl;

import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.dto.TimeTableDTO;
import com.demo.buslinesystem.bean.dto.TwoSnameWithCountDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.bean.po.UserPO;
import com.demo.buslinesystem.common.LineField;
import com.demo.buslinesystem.dao.*;
import com.demo.buslinesystem.service.LineService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LineServiceImpl implements LineService {

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Resource
    private LineDao lineDao;

    @Resource
    private StationDao stationDao;

    @Resource
    private RouteDao routeDao;

    @Resource
    private TimeTableDao timetableDao;


    @Override
    public List<String> toChName(List<String> alongStation) {
        List<String> chName = new ArrayList<>();
        for (int i = 0; i < alongStation.size(); i++) {
            chName.add(stationDao.getNameById(alongStation.get(i)));
        }
        return chName;
    }

    @Override
    public List<String> to_line_ChName(List<String> lines) {
        List<String> chName = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            chName.add(lineDao.getNameById(lines.get(i)));
        }
        return chName;
    }

    @Override
    public List<String> stationtoChName(String line) {
        Criteria criteria = Criteria.where(LineField.NAME).is(line);
        RoutePO routepo = mongoTemplate.findOne(Query.query(criteria), RoutePO.class, "routes");
        List<String> strings = toChName(routepo.getAlongStation());
        return strings;
    }

    @Override
    public List<String> directStationCount(String line) {
        List<RoutePO> routes = mongoTemplate.find(Query.query(Criteria.where(LineField.NAME).in(Lists.newArrayList(line + "路上行", line + "路下行"))), RoutePO.class, "routes");
        if (routes.size() == 0) {
            return Lists.newArrayList();
        }
        Set<String> stationIds = routes.stream().flatMap(e -> e.getAlongStation().stream()).collect(Collectors.toSet());
        List<StationPO> stationPOS = mongoTemplate.find(Query.query(Criteria.where("id").in(stationIds)), StationPO.class, "stations");
        Map<String, String> stationId2NameMap = stationPOS.stream().collect(Collectors.toMap(StationPO::getId, StationPO::getName, (a, b) -> a));
        List<List<String>> stationNameList = routes.stream().map(e -> e.getAlongStation().stream().map(stationId2NameMap::get).filter(Objects::nonNull).distinct().collect(Collectors.toList()))
            .collect(Collectors.toList());
        if (stationNameList.size() == 1) {
            return stationNameList.get(0);
        }
        List<String> union = ListUtils.union(stationNameList.get(0), stationNameList.get(1));
        union.removeAll(ListUtils.intersection(stationNameList.get(0), stationNameList.get(1)));
        return union;
    }

    @Override
    public List<TwoSnameWithCountDTO> straightSort(Integer option, Integer num) {

        List<StationPO> allStations = mongoTemplate.findAll(StationPO.class, "stations");
        List<String> allStationNames = allStations.stream().map(StationPO::getName).distinct().collect(Collectors.toList());

        Set<TwoSnameWithCountDTO> resultRoutes = new HashSet<>();
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        Map<String, String> stationNameToPOMap = allStations.stream()
            .collect(Collectors.toMap(StationPO::getId, StationPO::getName, (a, b) -> a));
        List<List<String>> allAlongStationNameList = allRoutes.stream()
            .map(e -> e.getAlongStation().stream()
                .map(stationNameToPOMap::get)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
        int i, j;
        for (i = 0; i < allStationNames.size(); i++) {

            String finalStation = allStationNames.get(i);

            for (j = i + 1; j < allStationNames.size(); j++) {

                String finalStation1 = allStationNames.get(j);
                if (Objects.equals(finalStation, finalStation1)) {
                    continue;
                }

//                List<List<String>> lists =
//                        long m = c
//                        .filter(e -> e.contains(finalStation) && e.contains(finalStation1))
//                        .count();
////                        .collect(Collectors.toList());
                long m = allAlongStationNameList.stream()
                    .filter(e -> e.contains(finalStation) && e.contains(finalStation1))
                    .filter(e -> e.indexOf(finalStation) < e.indexOf(finalStation1))
                    .count();
                long n = allAlongStationNameList.stream()
                    .filter(e -> e.contains(finalStation) && e.contains(finalStation1))
                    .filter(e -> e.indexOf(finalStation) > e.indexOf(finalStation1))
                    .count();
                if (("金河客运站(下客点)".equals(finalStation) && "金河客运站(终点站)".equals(finalStation1))
                    || "金河客运站(下客点)".equals(finalStation1) && "金河客运站(终点站)".equals(finalStation)) {
                    List<List<String>> route = allAlongStationNameList.stream().filter(e -> e.contains(finalStation) && e.contains(finalStation1)).collect(Collectors.toList());
                    for (List<String> r : route) {
                        log.warn("目标路线为：{}", r);
                    }
                }
//                resultRoutes.add(new TwoSnameWithCountDTO(finalStation, finalStation1, (int) m));
//                resultRoutes.add(new TwoSnameWithCountDTO(finalStation1, finalStation, (int) n));
            }
        }
//        List<UserPO> users = resultRoutes.stream()
//                .map(dto -> UserPO.builder()
//                        .station1(dto.getStation1())
//                        .station2(dto.getStation2())
//                        .num(dto.getCount())
//                        .build())
//                .collect(Collectors.toList());
//        userDao.saveBatch(users);

        List<TwoSnameWithCountDTO> result = new ArrayList<>();
        if (option == 0) {
            result = resultRoutes.stream()
                .limit(num)
                .collect(Collectors.toList());

        } else if (option == 1) {
            result = resultRoutes.stream()
                .sorted(Comparator.comparingInt(TwoSnameWithCountDTO::getCount))
                .limit(num)
                .collect(Collectors.toList());
        } else if (option == 2) {
            result = resultRoutes.stream()
                .sorted((a, b) -> b.getCount() - a.getCount())
                .limit(num)
                .collect(Collectors.toList());
        }
        return result;
    }


    @Override
    public Double nonRepeatCo(String line) {
        Query query = new Query(Criteria.where("name").is(line));
        RoutePO routePO = mongoTemplate.findOne(query, RoutePO.class);
//        List<nonRepeatDTO> result = new ArrayList<>();
        List<UserPO> userPO = mongoTemplate.findAll(UserPO.class);
        if (routePO == null) {
            return null;
        }
        double sum = 0.0;
        int size = routePO.getAlongStation().size();
        List<String> stations = toChName(routePO.getAlongStation());
        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }
                for (UserPO po : userPO) {
                    if (po.getStation1().equals(stations.get(i)) &&
                        po.getStation2().equals(stations.get(j))) {
                        sum += po.getNum() == 0 ? 0 : 1.0 / po.getNum();
                    }
                }
//                Criteria criteria = new Criteria();
//                criteria.orOperator(Criteria.where("staion1").is(routePO.getAlongStation().get(i)),Criteria.where("station2").is(routePO.getAlongStation().get(j)));
//                Query query2 = new Query(criteria);
//                UserPO userPO = mongoTemplate.findOne(query2, UserPO.class);
            }
            sum = (sum / (size ^ 2));
        }

        return sum;
    }

    @Override
    public List<LinePO> getLineInfo(String line) {
        if (line == null) {
            return null;
        }
        return lineDao.getLineInfo(line);
    }

    @Override
    public List<Map<String, List<String>>> getLineStop(String name) {
        List<String> station = stationDao.getIdByName(name);
        List<Map<String, List<String>>> map = routeDao.getLineStop(station);
        return map;
    }

    @Override
    public List<List<String>> getTimeTable(String name) {
        return timetableDao.getTimeTable(name);
    }

    @Override
    public List<String> getStation(String name) {
        List<RoutePO> routepos = routeDao.getRoute(name);
        List<RoutePO> results = stationDao.getNameById(routepos);
        if (results.get(0) != null) {
            return results.get(0).getAlongStation();
        }
        return null;
    }

    @Override
    public void add(String name, String alongStation) {
        List<String> result = Arrays.asList(alongStation.split(","));
        routeDao.add(name, result);
    }

    @Override
    public void edit(String name, String alongStation) {
        List<String> result = Arrays.asList(alongStation.split(","));
        routeDao.edit(name, result);
    }

    @Override
    public void delete(String name) {
        routeDao.delete(name);
    }

    @Override
    public List<StopLineDTO> getStopLine(String currenttime, String id, String minute) throws ParseException {
//        List<String> ids = stationDao.getIdByName(station);
        List<StopLineDTO> stoplinedtos = routeDao.getStopLine(id);
        List<String> times = getTimeList(currenttime, minute);
        List<StopLineDTO> result = timetableDao.getStopLine(stoplinedtos, times);
        return result;
    }

    @Override
    public List<String> getTimeList(String currenttime, String minute) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = sdf.parse(currenttime);
        List<String> times = new ArrayList<>();
        for (int i = 0; i <= Integer.parseInt(minute); i++) {
            String time = sdf.format(date.getTime() + i * 60 * 1000);
            times.add(time);
        }
        return times;
    }
}
