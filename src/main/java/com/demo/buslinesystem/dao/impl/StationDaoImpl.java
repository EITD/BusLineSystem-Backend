package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.*;
import com.demo.buslinesystem.dao.AdjacentStationDao;
import com.demo.buslinesystem.dao.StationDao;
import com.demo.buslinesystem.dao.UserDao;
import com.demo.buslinesystem.dao.impl.UserDaoImp.UnitKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StationDaoImpl implements StationDao {

    private final static String COLLECTION = "stations";

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserDao userDao;
    @Autowired
    private AdjacentStationDao adjacentStationDao;

    @Override
    public List<StationPO> queryByIds(List<String> stationIds) {
        if (CollectionUtils.isEmpty(stationIds)) {
            return Collections.emptyList();
        }
        return mongoTemplate.find(Query.query(Criteria.where("id").in(stationIds)), StationPO.class, "stations");
    }

    @Override
    public List<String> getIdByName(String station) {
        Criteria criteria = Criteria.where("name").is(station);
        List<StationPO> stationPOS = mongoTemplate.find(Query.query(criteria), StationPO.class, COLLECTION);
        List<String> ids = stationPOS.stream().map(StationPO::getId).collect(Collectors.toList());
        return ids;
    }

    @Override
    public String getNameById(String station) {
        Criteria criteria = Criteria.where("id").is(station);

        StationPO po = mongoTemplate.findOne(Query.query(criteria), StationPO.class, COLLECTION);

        return po.getName();
    }

    @Override
    public List<StationGroupDTO> stationCountByGroup() {
        //List<Integer> result = new ArrayList<>(4);
        //result.add(0); result.add(0); result.add(0); result.add(0);
        List<StationPO> allStations = mongoTemplate.findAll(StationPO.class, "stations");
        // List<RoutePO> allroutes = mongoTemplate.findAll(RoutePO.class, "routes");
        Map<String, Set<String>> result = new HashMap<>();
        allStations.forEach(station -> {
            if (station.getName().contains("地铁")) {
                Set<String> set1 = result.getOrDefault("地铁", new HashSet<>());
                set1.add(station.getName());
                result.put("地铁", set1);
            }
            if (station.getName().contains("始发站")) {
                Set<String> set1 = result.getOrDefault("始发站", new HashSet<>());
                set1.add(station.getName());
                result.put("始发站", set1);
            }
            if (station.getName().contains("终点站")) {
                Set<String> set1 = result.getOrDefault("终点站", new HashSet<>());
                set1.add(station.getName());
                result.put("终点站", set1);
            }

        });
        List<StationGroupDTO> finalResult = new ArrayList<>();
        finalResult = result.entrySet().stream().map(entry -> new StationGroupDTO(entry.getKey(), entry.getValue() == null ? 0 : entry.getValue().size(), entry.getValue())).collect(Collectors.toList());
        // return result.entrySet().stream().map(entry -> entry.getValue() == null ? 0 : entry.getValue().size()).collect(Collectors.toList());
        return finalResult;
    }



    @Override
    public List<UserPO> straightSort2(Integer option, Integer num) {
        Set<UserPO> users = this.createAllTwoStationChange();

        List<UserPO> result = new ArrayList<>();
        if (option == 0) {
            result = users.stream()
                    .limit(num)
                    .collect(Collectors.toList());

        } else if (option == 1) {
            result = users.stream()
                    .sorted(Comparator.comparingInt(UserPO::getNum))
                    .limit(num)
                    .collect(Collectors.toList());
        } else if (option == 2) {
            result = users.stream()
                    .sorted((a, b) -> b.getNum() - a.getNum())
                    .limit(num)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<UserPO> straightSort(Integer option, Integer num) {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        List<UserPO> userPOS = mongoTemplate.findAll(UserPO.class, "user");
        Map<UserDaoImp.UnitKey, UserPO> unitKey2UserMap = userPOS.parallelStream()
                .collect(Collectors.toMap(e -> new UserDaoImp.UnitKey(e.getId1(), e.getId2()), Function.identity(), (a, b) -> a));

        allRoutes.forEach(route -> {
            if ("N12路上行".equals(route.getName())) {
                log.info(route.getName());
            }
            List<String> allStation = route.getAlongStation();
            for (int i = 0; i < allStation.size() - 1; i++) {
                String id1 = allStation.get(i);
                String id2 = allStation.get(i + 1);
                UserDaoImp.UnitKey key = new UserDaoImp.UnitKey(id1, id2);
//                unitKey2UserMap.compute(key, (k, v) -> {
//                    if (v == null) {
//                        return UserPO.builder().id1(id1).id2(id2).adjacent(1).line(route.getName()).build();
//                    } else {
//
//                        v.setAdjacent(1);
//                        v.setLine(route.getName());
//                        return v;
//                    }
//                });
            }
        });
        List<UserPO> userPOList = new ArrayList(unitKey2UserMap.values());
        userDao.saveBatch(userPOList);
        return null;


    }

    @Override
    public List<UserPO> twoStationLine(Integer option, Integer num) {
        if (option == 0) {
            return  mongoTemplate.find(Query.query(Criteria.where("adjacent").is(1)).limit(num), UserPO.class, "user");
        }
        List<UserPO> userPOList = mongoTemplate.find(Query.query(Criteria.where("adjacent").is(1)).with(Sort.by(option == 1 ? Sort.Order.asc("num") : Sort.Order.desc("num"))).limit(num), UserPO.class, "user");
//        List<UserPO> userPOS2 = mongoTemplate.find(new Query().with(Sort.by(option == 1 ? Sort.Order.asc("num") : Sort.Order.desc("num"))), UserPO.class, "user")).stream().filter(u -> u.getAdjacent() == 1).limit(num).collect(Collectors.toList());
        return userPOList;
    }

    @Override
    public Object[] selectStations() {
        List<findLinesDTO> result = new ArrayList<>();
        List<StationPO> stationPOS = mongoTemplate.findAll(StationPO.class);
        HashSet<String> stations = new HashSet<>();
        stationPOS.forEach(r -> {
            stations.add(r.getName());
        });
        stations.forEach(r -> {
            result.add(findLinesDTO.builder().value(r).build());
        });
        Object[] objects = result.toArray();
        return objects;
    }

    @Override
    public Object[] selectStationsById() {
        List<findLinesDTO> result = new ArrayList<>();
        List<StationPO> stationPOS = mongoTemplate.findAll(StationPO.class);
        stationPOS.forEach(r -> {
            result.add(findLinesDTO.builder().value(r.getId()).build());
        });
        Object[] objects = result.toArray();
        return objects;
    }

    @Override
    public Set<UserPO> createAllTwoStationChange() {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        List<StationPO> allStations = mongoTemplate.findAll(StationPO.class, "stations");
        Set<UserPO> users = new HashSet<>();

        for (int i = 0; i < allStations.size(); i++) {
            for (int j = i + 1; j < allStations.size(); j++) {
                String beginStationId = allStations.get(i).getId();
                String endStationId = allStations.get(j).getId();
                if (StringUtils.equals(beginStationId, endStationId)) {
                    continue;
                }
                Set<RoutePO> collect = allRoutes.stream()
                    .filter(e -> e.getAlongStation().contains(beginStationId) && e.getAlongStation().contains(endStationId))
                    .collect(Collectors.toSet());
                List<RoutePO> ascRoutes = collect.stream()
                    .filter(e -> e.getAlongStation().indexOf(beginStationId) < e.getAlongStation().indexOf(endStationId))
                    .collect(Collectors.toList());
                List<RoutePO> dascRoutes = collect.stream()
                    .filter(e -> e.getAlongStation().indexOf(beginStationId) >= e.getAlongStation().indexOf(endStationId))
                    .collect(Collectors.toList());
                int m = ascRoutes.size();
                if (m != 0) {
                    int finalI = i;
                    int finalJ = j;
                    users.addAll(ascRoutes.stream().map(e -> UserPO.builder()
                        .station1(allStations.get(finalI).getName()).id1(beginStationId)
                        .station2(allStations.get(finalJ).getName()).id2(endStationId)
                        .num(m).routeName(e.getName()).build())
                        .collect(Collectors.toList()));
                }
                if ((collect.size() - m) != 0) {
                    int finalI = i;
                    int finalJ = j;
                    users.addAll(dascRoutes.stream().map(e -> UserPO.builder()
                        .station1(allStations.get(finalJ).getName()).id1(endStationId)
                        .station2(allStations.get(finalI).getName()).id2(beginStationId)
                        .num(m).routeName(e.getName()).build())
                        .collect(Collectors.toList()));
                }
            }
        }
        // 32169
        if (!mongoTemplate.collectionExists("user")) {
            userDao.saveBatch(new ArrayList<>(users));
        }
        return users;
    }

    @Override
    public Set<AdjacentStationPO> createAdjacentStationChange() {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");

        List<StationPO> stations = mongoTemplate.findAll(StationPO.class, "stations");
        Map<String, StationPO> id2StationMap = stations.stream().collect(Collectors.toMap(StationPO::getId, Function.identity(), (a, b) -> a));

        Set<AdjacentStationPO> adjacentStationPOS = new HashSet<>();
        allRoutes.forEach(route -> {
            if ("N12路上行".equals(route.getName())) {
                log.info(route.getName());
            }
            List<String> allStation = route.getAlongStation();
            for (int i = 0; i < allStation.size() - 1; i++) {
                String id1 = allStation.get(i);
                String id2 = allStation.get(i + 1);
                if (id2StationMap.get(id1) == null) {
                    log.info("null");
                }
                adjacentStationPOS.add(AdjacentStationPO.builder()
                    .station1(id2StationMap.get(id1).getName())
                    .id1(id2StationMap.get(id1).getId())
                    .station2(id2StationMap.get(id2).getName())
                    .id2(id2StationMap.get(id2).getId())
                    .routeName(route.getName())
                    .build());
            }
        });
        if (!mongoTemplate.collectionExists("adjacent_station")) {
            adjacentStationDao.saveBatchInit(adjacentStationPOS);
        }
        return adjacentStationPOS;
    }

    @Override
    public List<AdjacentStationDTO> queryAdjacentStationChangeLine(Integer option, Integer num) {
        List<AdjacentStationPO> adjacentStations = mongoTemplate.findAll(AdjacentStationPO.class, "adjacent_station");
        Map<String, List<AdjacentStationPO>> stations2POMap = adjacentStations.stream().collect(Collectors.groupingBy(e -> e.getId1() + ":" + e.getId2()));
        List<AdjacentStationDTO> dtos = stations2POMap.keySet().stream().map(e -> {
            List<AdjacentStationPO> adjacentStationPOS = stations2POMap.get(e);
            AdjacentStationPO first = adjacentStationPOS.get(0);
            return AdjacentStationDTO.builder().station1(first.getStation1()).id1(first.getId1())
                .station2(first.getStation2()).id2(first.getId2())
                .routeName(first.getRouteName()).count(adjacentStationPOS.size())
                .build();
        }).collect(Collectors.toList());
        List<AdjacentStationDTO> adjacentStationDTOS = dtos.stream().sorted((a, b) -> {
            if (option == 1) {
                return a.getCount() - b.getCount();
            }
            if (option == 2) {
                return b.getCount() - a.getCount();
            }
            return 0;
        }).limit(num).collect(Collectors.toList());
        return adjacentStationDTOS;
    }


    @Override
    public List<StationStatisticsDTO> sortById(Integer option, Integer num) {

        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");

        TreeMap<String, List<String>> statisticsMap = new TreeMap<>();
        allRoutes.forEach(route -> {
            List<String> allStationIds = route.getAlongStation();
            allStationIds.forEach(stationId -> {
                statisticsMap.compute(stationId, (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>();
                    }
                    v.add(route.getName());
                    return v;
                });
            });
        });
        List<Map.Entry<String, List<String>>> sortList = new ArrayList<>(statisticsMap.entrySet());
        if (option == 1) {
            sortList.sort((a, b) -> a.getValue().size() - b.getValue().size());
        } else if (option == 2) {
            sortList.sort((a, b) -> b.getValue().size() - a.getValue().size());
//            Collections.reverse(sortList);
        }

        long begin = System.currentTimeMillis();

        List<String> allStationIds = sortList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        log.info("stream：{}ms", end-begin);
        List<StationPO> allStationPOS = mongoTemplate.find(Query.query(Criteria.where("id").in(allStationIds)),
                StationPO.class, COLLECTION);


        Map<String, StationPO> idStationPOMap = allStationPOS.stream()
                .collect(Collectors.toMap(StationPO::getId, Function.identity(), (a, b) -> a));

        List<StationStatisticsDTO> stationStatisticsDTOS = sortList.stream()
//                .map(entry -> StationStatisticsDTO.builder()
//                        .station(idStationPOMap.get(entry.getKey()))
//                        .count(entry.getValue())
//                        .build())
                .map(entry -> new StationStatisticsDTO(idStationPOMap.get(entry.getKey()), entry.getValue(), entry.getValue().size() + 1))
                .filter(dto -> dto.getStation() != null)
                .collect(Collectors.toList());
        return stationStatisticsDTOS.subList(0, num);
    }

    @Override
    public List<RoutePO> getNameById(List<RoutePO> list) {
        List<StationPO> stationPOS = mongoTemplate.findAll(StationPO.class);
        Map<String, String> map = new HashMap<String, String>();
        stationPOS.forEach(station -> {
            map.put(station.getId(), station.getName());
        });
//        for(StationPO station : stationPOS){
//            map.put(station.getId(),station.getName());
//        }
        if(list.get(0)!=null){
            list.forEach(route -> {
                        route.getAlongStation().forEach(station -> {
                                    String str = map.get(station);
                                    route.getAlongStation().set(route.getAlongStation().indexOf(station), str);
                                }
                        );
                    }
            );
        }
//        for(RoutePO route : list){
//            int length = route.getAlongStation().size();
//            for(int i=0;i<length;i++){
//                String str = map.get(route.getAlongStation().get(i));
//                route.getAlongStation().set(i,str);
//            }
//        }
        return list;
    }

    @Override
    public List<StationDTO> getStation(List<RoutePO> route) {
//        List<StationPO> stationpos = mongoTemplate.findAll(StationPO.class);
        List<StationDTO> list = new ArrayList<>();
        if(route.get(0)!=null){
            route.forEach(r -> {
                r.getAlongStation().forEach(station -> {
                    StationDTO stationdto = new StationDTO();
                    stationdto.setLinename(r.getName());
                    Criteria criteria = Criteria.where("id").is(station);
                    StationPO stationPO = mongoTemplate.findOne(Query.query(criteria),StationPO.class);
                    stationdto.setId(stationPO.getId());
                    stationdto.setStationname(stationPO.getName());
                    stationdto.setEnglish(stationPO.getEnglish());
                    list.add(stationdto);
                });
            });
        }
        return list;
    }

    @Override
    public List<LineDTO> getNameById2(List<LineDTO> list) {
        List<LineDTO> resultlist = new ArrayList<>();
        list.forEach(line -> {
            line.getAlongwaystation().forEach(station -> {
                LineDTO linedto = new LineDTO();
                linedto.setLinename(line.getLinename());
                linedto.setTime(line.getTime());
                Criteria criteria = Criteria.where("id").is(station);
                StationPO stationPO = mongoTemplate.findOne(Query.query(criteria), StationPO.class);
                linedto.setId(stationPO.getId());
                linedto.setStationname(stationPO.getName());
                linedto.setEnglish(stationPO.getEnglish());
                resultlist.add(linedto);
            });
        });
        return resultlist;
    }

    @Override
    public void delete(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        mongoTemplate.remove(Query.query(criteria), StationPO.class, COLLECTION);
    }

//        List<StationPO> stationPOS = mongoTemplate.findAll(StationPO.class);
//        Map<String, String> map = new HashMap<String, String>();
//        stationPOS.forEach(station -> {
//            map.put(station.getId(), station.getName());
//        });
//        list.forEach(line -> {
//                    line.getAlongwaystation().forEach(station -> {
//                                String str = map.get(station);
//                                line.getAlongwaystation().set(line.getAlongwaystation().indexOf(station), str);
//                            }
//                    );
//                }
//        );
//        return list;
//    }

    @Override
    public List<ShortPO> createShort() {
        List<ShortPO> shortPOS = new ArrayList<>();
        List<StationPO> stationPOS = mongoTemplate.findAll(StationPO.class);
        List<RoutePO> routePOS = mongoTemplate.findAll(RoutePO.class);
        stationPOS.forEach(station -> {
            ShortPO shortPO = new ShortPO();
            shortPO.setId(station.getId());
            List<RoutePO> list = routePOS.stream()
                    .filter(route -> route.getAlongStation().contains(shortPO.getId()))
                    .collect(Collectors.toList());
            List<List<String>> next = new ArrayList<>();
            list.forEach(route -> {
                List<String> temp = route.getAlongStation()
                        .subList(route.getAlongStation().indexOf(shortPO.getId()), route.getAlongStation().size());
                next.add(temp);
            });
            shortPO.setNext(next);
            shortPOS.add(shortPO);
        });
        return shortPOS;
    }

    @Override
    public List<StationPO> findShort(List<String> ids) {
//        List<StationPO> stationpos = mongoTemplate.findAll(StationPO.class);
        List<StationPO> result = new ArrayList<>();
        if(ids!=null){
            ids.forEach(id->{
                Criteria criteria = Criteria.where("id").is(id);
                StationPO stationPO = mongoTemplate.findOne(Query.query(criteria),StationPO.class);
                result.add(stationPO);
            });
        }
//        List<StationPO> result = stationpos.stream().filter(po-> ids.contains(po.getId()))
//                .collect(Collectors.toList());
        return result;
    }
}
