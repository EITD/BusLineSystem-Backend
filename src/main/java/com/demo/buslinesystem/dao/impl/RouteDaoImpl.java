package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.dto.LStaCountDTO;
import com.demo.buslinesystem.bean.dto.RepeatStationDTO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.bean.po.UserPO;
import com.demo.buslinesystem.dao.RouteDao;

import java.util.*;
import com.demo.buslinesystem.dao.StationDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;


import com.demo.buslinesystem.dao.UserDao;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class RouteDaoImpl implements RouteDao {

    private static final String COLLECTION = "routes";

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Resource
    private StationDao stationDao;

    @Resource
    private UserDao userDao;

    @Override
    public List<RoutePO> queryRoutesExist(List<String> ids1, List<String> ids2) {
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        List<RoutePO> resultList = allRoutePOS.stream()
            .filter(po -> ListUtils.intersection(po.getAlongStation(), ids1).size() != 0
                && ListUtils.intersection(po.getAlongStation(), ids2).size() != 0)
            .filter(po -> po.getAlongStation().indexOf(ListUtils.intersection(po.getAlongStation(), ids1).get(0))
                <= po.getAlongStation().indexOf(ListUtils.intersection(po.getAlongStation(), ids2).get(0)))
            .collect(Collectors.toList());
       return resultList;
   }

//    @Override
//    public List<UserPO> TwoStationLineSort(Integer option, Integer num) {
//        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
//
//        Set<TwoSnameWithCountDTO> resultRoutes = new HashSet<>();
//        Set<UserPO> users = new HashSet<>();
//
//        int i, j, a, b;
//
//        for (i = 0; i < allRoutes.size(); i++) {
//            List<String> routeStations = allRoutes.get(i).getAlongStation();
//            if(routeStations.size()<2){
//                return null;
//            }
//            for (a = 0, b = 1; b < routeStations.size(); a++, b++) {
//                String finalStation = routeStations.get(a);
//                String finalStation1 = routeStations.get(b);
//                List<RoutePO> collect = allRoutes.stream()
//                        .filter(e -> e.getAlongStation().contains(finalStation) && e.getAlongStation().contains(finalStation1))
//                        .filter(e -> e.getAlongStation().indexOf(finalStation) < e.getAlongStation().indexOf(finalStation1))
//                        .collect(Collectors.toList());
//                long m = collect.size();
//
//                Criteria criteria = Criteria.where("id").is(finalStation);
//                StationPO po = mongoTemplate.findOne(Query.query(criteria), StationPO.class, "stations");
//                Criteria criteria2 = Criteria.where("id").is(finalStation1);
//                StationPO po2 = mongoTemplate.findOne(Query.query(criteria2), StationPO.class, "stations");
//
//                users.add(new UserPO(
//                        po.getName(),
//                        po.getId(),
//                        po2.getName(),
//                        po2.getId(),
//                        allRoutes.get(i).getName(),
//                        (int) m));
//            }
//        }
//
//        userDao.saveBatch(new ArrayList<>(users));
//
//        List<UserPO> result = new ArrayList<>();
//        if (option == 0) {
//            result = users.stream()
//                    .limit(num)
//                    .collect(Collectors.toList());
//
//        } else if (option == 1) {
//            result = users.stream()
//                    .sorted(Comparator.comparingInt(UserPO::getNum))
//                    .limit(num)
//                    .collect(Collectors.toList());
//        } else if (option == 2) {
//            result = users.stream()
//                    .sorted((m, n) -> n.getNum() - m.getNum())
//                    .limit(num)
//                    .collect(Collectors.toList());
//        }
//        return result;
//    }


    @Override
    public List<String> repeatSid(String station1, String station2) {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class,"routes");
        List<String> result = new ArrayList<>();
        allRoutes.forEach(route->{
            AtomicReference<Integer> flag= new AtomicReference<>(1);
            route.getAlongStation().forEach(station->{
                if(station==station1)
                {
                    flag.set(0);
                }
                if(station==station2){
                    flag.set(0);
                }

            });
            if(flag.get()==1)
            {
                result.add(route.getName());
            }
        });

        return result;
    }

    @Override
    public List<String> repeatStation(String line1, String line2) {

        List<RepeatStationDTO> result =new ArrayList<>();


        Criteria criteria1 = Criteria.where("name").is(line1);
        Criteria criteria2 = Criteria.where("name").is(line2);
        RoutePO route1 =  mongoTemplate.findOne(Query.query(criteria1),RoutePO.class,COLLECTION);
        RoutePO route2 =  mongoTemplate.findOne(Query.query(criteria2),RoutePO.class,COLLECTION);
        if(route1==null||route2==null){ return new ArrayList<>();}
        else{
        String[] route1List = route1.getAlongStation().toArray(new String[0]);
        String[] route2List = route2.getAlongStation().toArray(new String[0]);
        List<String> name =new ArrayList<>();
        getAllSameElement1route1(route1List,route2List).forEach(station->{
            name.add(stationDao.getNameById(station));
                }

        );
        //result = RepeatStationDTO.builder().name(name).count(name.size()).build().;

        return name;
        }
    }

    private List<String> getAllSameElement1route1(String[] strArr1, String[] strArr2) {
        if(strArr1 == null || strArr2 == null) {
            return null;
        }
        List<String> strList1 = new ArrayList<String>(Arrays.asList(strArr1)); //----------代码段1
        List<String>  strList2 = new ArrayList<String>(Arrays.asList(strArr2));  //--------------代码段2

        strList1.retainAll(strList2);
        return strList1;
    }


    @Override
    public List<LStaCountDTO> sortBySNum(Integer option, Integer num) {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        List<LStaCountDTO> result = new ArrayList<>();
        if(option==0) {
            result = allRoutes.stream()
                    .limit(num)
                    .map(e -> LStaCountDTO.builder().route(e).count(e.getAlongStation().size()).build())
                    .collect(Collectors.toList());

        }
        else if(option==1) {
            result = allRoutes.stream()
                    .sorted(Comparator.comparingInt(a -> a.getAlongStation().size()))
                    .limit(num)
                    .map(e -> LStaCountDTO.builder().route(e).count(e.getAlongStation().size()).build())
                    .collect(Collectors.toList());
        }
        else if(option==2) {
            result = allRoutes.stream()
                    .sorted((a, b) -> b.getAlongStation().size() - a.getAlongStation().size())
                    .limit(num)
                    .map(e -> LStaCountDTO.builder().route(e).count(e.getAlongStation().size()).build())
                    .collect(Collectors.toList());
        }
        return result;

    }

    @Override
    public List<S1L1DTO> cRoadCount(String name) {
        Criteria criteria = Criteria.where("name").is(name);
        RoutePO route = mongoTemplate.findOne(Query.query(criteria), RoutePO.class,"routes");
        List<StationPO> station = mongoTemplate.findAll(StationPO.class,"stations");
        Map<String,String > name_id = new HashMap<>();
        station.forEach(s->{
            name_id.put(s.getId(),s.getName());
        });

        if (route == null) {
            return null;
        }
        List<S1L1DTO> routesDTO = new ArrayList<>();
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        Map<String,Set<String>> finalResult = new HashMap<>();
            allRoutes.forEach(e-> {
                List<String> inserStation = ListUtils.intersection(route.getAlongStation(), e.getAlongStation());
                if (inserStation.size() != 0) {
                    inserStation.forEach(iStation -> {
                                Set<String> s = finalResult.getOrDefault(name_id.get(iStation), new HashSet<>());
                                if(!e.getName().equals(route.getName()))
                                s.add(e.getName());
                                finalResult.put(name_id.get(iStation),s);
                            }
                    );

                }
            });
        for(HashMap.Entry<String,Set<String>> entry : finalResult.entrySet()) {
            routesDTO.add(S1L1DTO.builder().name(entry.getKey()).routes(new ArrayList<>(entry.getValue()) ).build());
        }

        return routesDTO;


    }

    @Override
    public List<LStaCountDTO> changeSort(Integer option, Integer num) {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        Map<String, Long> stationCountMap = allRoutes.stream()
            .flatMap(e -> e.getAlongStation().stream())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<LStaCountDTO> countDTOS = new ArrayList<>();
        for (RoutePO route : allRoutes) {
            List<String> stationIds = route.getAlongStation();
            long count = 0;
            for (String id : stationIds) {
                //统计每个站点在所有路线中出现的次数
                long perCount = stationCountMap.getOrDefault(id, 0L);
                count += perCount == 1 ? 0 : perCount;
            }
            countDTOS.add(LStaCountDTO.builder().route(route).count((int) count).build());
        }
        List<LStaCountDTO> result = countDTOS.stream()
            .sorted((a, b) -> {
                if (option == 1) {
                    return a.getCount() - b.getCount();
                }
                if (option == 2) {
                    return b.getCount() - a.getCount();
                }
                return 0;
            })
            .limit(num).collect(Collectors.toList());
        return result;
    }

    @Override
    public Integer straightRoadCount(String station1,String station2) {
        List<RoutePO> allRoutes = mongoTemplate.findAll(RoutePO.class, "routes");
        long result=allRoutes.stream()
                .filter(e-> e.getAlongStation().contains(station1)&&e.getAlongStation().contains(station2))
                .count();
        return (int)result;
    }

    @Override
    public List<LineDTO> getAlongway(String line, List<String> ids1, List<String> ids2){
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        List<RoutePO> resultList = allRoutePOS.stream()
                .filter(po -> po.getName().equals(line) || po.getName().equals(line+"上行") || po.getName().equals(line+"下行"))
                .filter(po -> ListUtils.intersection(po.getAlongStation(), ids1).size() != 0
                        && ListUtils.intersection(po.getAlongStation(), ids2).size() != 0)
                .filter(po -> po.getAlongStation().indexOf(ListUtils.intersection(po.getAlongStation(), ids1).get(0))
                        <= po.getAlongStation().indexOf(ListUtils.intersection(po.getAlongStation(), ids2).get(0)))
                .collect(Collectors.toList());
        List<LineDTO> list = new ArrayList<>();
        resultList.forEach(route -> {
            LineDTO linedto = new LineDTO();
            linedto.setLinename(route.getName());
            int start = route.getAlongStation().indexOf(ListUtils.intersection(route.getAlongStation(), ids1).get(0));
            int end =route.getAlongStation().indexOf(ListUtils.intersection(route.getAlongStation(), ids2).get(0));
            linedto.setStart(start);
            linedto.setEnd(end);
            List<String> liststr = route.getAlongStation().subList(start, end+1);
            linedto.setAlongwaystation(liststr);
            list.add(linedto);
        });
        return list;
    }

    @Override
    public List<LatestDTO> getLatest(String id){
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        List<RoutePO> resultList = allRoutePOS.stream()
                .filter(po -> po.getAlongStation().contains(id))
                .collect(Collectors.toList());
        List<LatestDTO> latestdtos = new ArrayList<>();
        resultList.forEach(route -> {
            LatestDTO latestdto = new LatestDTO();
            latestdto.setName(route.getName());
            latestdto.setIndex(route.getAlongStation().indexOf(id));
            latestdtos.add(latestdto);
        });
        return latestdtos;
    }

    @Override
    public void batchUpdateRoute(List<RoutePO> routePOS) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, COLLECTION);
        List<Pair<Query, Update>> updateList = new ArrayList<>();
        routePOS.forEach(po -> {
            Query query = Query.query(Criteria.where("name").is(po.getName()));
            Update update = Update.update("onewayTime", po.getOnewayTime());
            updateList.add(Pair.of(query, update));
        });
        bulkOperations.updateMulti(updateList);
        bulkOperations.execute();
    }

    @Override
    public Object[] selectLines() {
        List<findLinesDTO> result = new ArrayList<>();
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        allRoutePOS.forEach(r->{
            result.add(findLinesDTO.builder().value(r.getName()).build());
        });
        Object[] objects = result.toArray();
        return objects;
    }

    @Override
    public List<StopLineDTO> getStopLine(String id){
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        List<StopLineDTO> stoplinedtos = new ArrayList<>();
        List<RoutePO> routepos = allRoutePOS.stream()
                    .filter(po -> (po.getAlongStation().contains(id)))
                    .collect(Collectors.toList());
        routepos.forEach(route -> {
                StopLineDTO stoplinedto = new StopLineDTO();
                stoplinedto.setName(route.getName());
                stoplinedto.setIndex(route.getAlongStation().indexOf(id));
                stoplinedtos.add(stoplinedto);
        });
        return stoplinedtos;
    }

    @Override
    public List<Map<String,List<String>>> getLineStop(List<String> ids){
        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
        List<Map<String,List<String>>> list = new ArrayList<>();
        ids.forEach(id ->{
            List<String> idlist = new ArrayList<String>();
            idlist.add(id);
            List<String> names = allRoutePOS.stream()
                    .filter(po -> ListUtils.intersection(po.getAlongStation(), idlist).size() != 0)
                    .map(RoutePO::getName)
                    .collect(Collectors.toList());
            Map<String,List<String>> map = new HashMap<String,List<String>>();
            map.put("id",idlist);
            map.put("key",names);
            list.add(map);
        });
        return list;
    }

    @Override
    public int findTotal(){
        Query query = new Query();
        return (int)mongoTemplate.count(query,COLLECTION);
    }

//    @Override
//    public List<UserPO> TwoStationLineSort(Integer option, Integer num) {
//        return null;
//    }

    @Override
    public List<RoutePO> getRouteByPage(int start, int pagesize){
        Query query = new Query();
        query.skip(start).limit(pagesize).with(Sort.by(Sort.Order.asc("name")));
        return mongoTemplate.find(query,RoutePO.class);
    }

    @Override
    public List<RoutePO> getRoute(String name){
        return mongoTemplate.find(Query.query(Criteria.where("name").is(name)), RoutePO.class, COLLECTION);
    }

    @Override
    public RoutePO queryRoute(String name) {
        return mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), RoutePO.class, COLLECTION);
    }

    @Override
    public void add(String name,List<String> alongStation){
        RoutePO routepo = new RoutePO();
        routepo.setName(name);
        routepo.setAlongStation(alongStation);
        mongoTemplate.insert(routepo);
    }

    @Override
    public void edit(String name,List<String> alongStation){
        Criteria criteria = Criteria.where("name").is(name);
        Update update = new Update();
        update.set("alongStation", alongStation);
        mongoTemplate.upsert(Query.query(criteria),update,RoutePO.class,COLLECTION);
//        route.setAlongStation(alongStation);

    }

    @Override
    public void delete(String name){
        Criteria criteria = Criteria.where("name").is(name);
//        RoutePO route = mongoTemplate.findOne(Query.query(criteria), RoutePO.class, COLLECTION);
//        List<String> alongStation = route.getAlongStation();
//        List<RoutePO> allRoutePOS = mongoTemplate.findAll(RoutePO.class);
//        alongStation.forEach(station->{
//            List<RoutePO> exist = allRoutePOS.stream()
//                    .filter(po -> po.getName()!=name&&po.getAlongStation().contains(station))
//                    .collect(Collectors.toList());
//            if(exist.isEmpty())stationDao.delete(station);
//        });
        mongoTemplate.remove(Query.query(criteria),RoutePO.class,COLLECTION);
    }

x    @Override
    public RoutePO getalongStation(String name){
        Criteria criteria = Criteria.where("name").is(name);
        RoutePO route = mongoTemplate.findOne(Query.query(criteria), RoutePO.class, COLLECTION);
//        RouteDTO routedto = new RouteDTO(route.getAlongStation().toString().replace("[","").replace("]",""));
        return route;
    }
}
