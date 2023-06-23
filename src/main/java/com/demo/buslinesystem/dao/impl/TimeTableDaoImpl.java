package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.dto.*;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.TimeTablePO;
import com.demo.buslinesystem.dao.RouteDao;
import com.demo.buslinesystem.dao.TimeTableDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimeTableDaoImpl implements TimeTableDao {

    private final static String COLLECTION = "timetables";

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;
    @Resource
    private RouteDao routeDao;

    @Override
    public List<LineTimeDTO> rSortByRuntime(Integer option, Integer num) {

        List<LineTimeDTO> result = new ArrayList<>();
        List<LineTimeDTO> result1 = new ArrayList<>();

//
//        List<TimeTablePO> allTimeTablePOs = mongoTemplate.findAll(TimeTablePO.class);
//        HashSet<TimeTablePO> timeTablePOS = new HashSet<>(allTimeTablePOs);
//
//        List<RoutePO> updateRoutes = new ArrayList<>();
//
//        timeTablePOS.forEach(t->{
//
//            List<String> s = t.getTimetable().get(0);
//            Integer size = s.size();
//            String[] a = s.get(size-1).split(":");
//            String[] b = s.get(0).split(":");
//            int endTime = Integer.parseInt(a[0]) * 60 + Integer.parseInt(a[1]);
//            int beginTime = Integer.parseInt(b[0]) * 60 + Integer.parseInt(b[1]);
//            Integer r = (endTime-beginTime) < 0 ? 1440 + endTime -beginTime  : endTime-beginTime;
//            updateRoutes.add(RoutePO.builder().name(t.getName()).onewayTime(r).build());
//
//        });
//
//        routeDao.batchUpdateRoute(updateRoutes);


        List<RoutePO> allRoutePOs = mongoTemplate.findAll(RoutePO.class);

        allRoutePOs.forEach(r -> {
            if (r.getOnewayTime() == null) {
                return;
            }
            result.add(LineTimeDTO.builder().route(r).time(r.getOnewayTime()).build());
        });

        if(option==0) {
            result1 = result.stream()
                    .limit(num).collect(Collectors.toList());
        }
        else  if(option==1){
            result1 = result.stream()
                    .sorted(Comparator.comparingInt(LineTimeDTO::getTime))
                    .limit(num).collect(Collectors.toList());

        }
        else  if(option==2){
            result1 = result.stream()
                    .sorted((a,b)->b.getTime()-a.getTime())
                    .limit(num).collect(Collectors.toList());

        }

        return result1;
    }

    @Override
    public List<List<String>> getTimeTable(String name){
        Criteria criteria = Criteria.where("name").is(name);
        TimeTablePO timetablepo = mongoTemplate.findOne(Query.query(criteria), TimeTablePO.class, COLLECTION);
        if(timetablepo==null)return null;
        else{
            List<List<String>> timetable = timetablepo.getTimetable();
            List<List<String>> timetabledtos = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(1);
            timetable.forEach(stationtimetable ->{
                List<String> strs = new ArrayList<>();
                strs.add("班次"+count);
                count.getAndIncrement();
                strs.addAll(stationtimetable);
                timetabledtos.add(strs);
            });
            return timetabledtos;
        }
    }

    @Override
    public List<LatestDTO> getLatest(List<LatestDTO> latestdtos, String time){
        List<LatestDTO> result = new ArrayList<>();
        latestdtos.forEach(latestdto -> {
            Criteria criteria = Criteria.where("name").is(latestdto.getName());
            TimeTablePO timetable = mongoTemplate.findOne(Query.query(criteria),TimeTablePO.class,COLLECTION);
            List<List<String>> list = timetable.getTimetable();
            try {
                if(getTime(list.get(timetable.getTimetable().size()-1).get(latestdto.getIndex()),time)<0){
                    latestdto.setLatest1("当前时间暂无班次");
                    latestdto.setLatest2("当前时间暂无班次");
                    latestdto.setLatest3("当前时间暂无班次");
                    result.add(latestdto);
                }
                else{
                    for(int i=0;i<list.size();i++){
                        if(getTime(list.get(i).get(latestdto.getIndex()),time)==0){
                            latestdto.setLatest1("即将到站");
                            latestdto.setLatest2(i+1<list.size()?
                                    getTime(list.get(i+1).get(latestdto.getIndex()),time)+"分钟后到站":"当前时间暂无班次");
                            latestdto.setLatest3(i+2<list.size()?
                                    getTime(list.get(i+2).get(latestdto.getIndex()),time)+"分钟后到站":"当前时间暂无班次");
                            result.add(latestdto);
                            break;
                        }
                        if(getTime(list.get(i).get(latestdto.getIndex()),time)>0){
                            latestdto.setLatest1(getTime(list.get(i).get(latestdto.getIndex()),time)+"分钟后到站");
                            latestdto.setLatest2(i+1<list.size()?
                                    getTime(list.get(i+1).get(latestdto.getIndex()),time)+"分钟后到站":"当前时间暂无班次");
                            latestdto.setLatest3(i+2<list.size()?
                                    getTime(list.get(i+2).get(latestdto.getIndex()),time)+"分钟后到站":"当前时间暂无班次");
                            result.add(latestdto);
                            break;
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    @Override
    public List<StopLineDTO> getStopLine(List<StopLineDTO> stoplinedtos, List<String> times){
        List<StopLineDTO> result = new ArrayList<>();
        stoplinedtos.forEach(stoplinedto -> {
            Criteria criteria = Criteria.where("name").is(stoplinedto.getName());
            TimeTablePO timetablepo = mongoTemplate.findOne(Query.query(criteria),TimeTablePO.class,COLLECTION);
            List<List<String>> list = timetablepo.getTimetable().stream()
                            .filter(t -> times.contains(t.get(stoplinedto.getIndex())))
                            .collect(Collectors.toList());
            if (!list.isEmpty()) {
                try {
                    int time = getTime(list.get(0).get(stoplinedto.getIndex()), times.get(0));
                    String linetime;
                    if(time==0){
                        linetime = "即将到站";
                    }
                    else {
                        linetime =time + "分钟后" + "到站";
                    }
                    stoplinedto.setLineTime(linetime);
                    result.add(stoplinedto);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
//        Query query = new Query();
//        List<TimeTablePO> allTimeTablePOS = mongoTemplate.findAll(TimeTablePO.class);
//        List<StopLineDTO> result = new ArrayList<>();
//        List<TimeTablePO> timetablepos = allTimeTablePOS.stream()
//                    .filter(po -> stoplinedto.getLines().contains(po.getName()))
////                            && po.getTimetable().stream()
////                            .filter(t -> times.contains(t.get(stoplinedto.getMap().get(po.getName())))))
//                    .collect(Collectors.toList());
//        timetablepos.forEach(timetablepo -> {
//            List<List<String>> timetable = timetablepo.getTimetable().stream()
//                            .filter(t -> times.contains(t.get(stoplinedto.getMap().get(timetablepo.getName()))))
//                            .collect(Collectors.toList());
//            if (!timetable.isEmpty()) {
//                try {
//                    StopLineDTO stopline = new StopLineDTO();
//                    int time = getTime(timetable.get(0).get(stoplinedto.getMap().get(timetablepo.getName())), times.get(0));
//                    String linetime;
//                    if(time==0){
//                        linetime = "即将到站";
//                    }
//                    else {
//                        linetime =time + "分钟后" + "到站";
//                    }
//                    stopline.setName(timetablepo.getName());
//                    stopline.setLineTime(linetime);
//                    if (!result.contains(stopline)) result.add(stopline);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        return result;
    }

//    @Override
//    public List<TimeTablePO> test(StopLineDTO stoplinedto, List<String> times){
//        List<TimeTablePO> allTimeTablePOS = mongoTemplate.findAll(TimeTablePO.class);
//        List<TimeTablePO> timetablepos = allTimeTablePOS.stream()
//                    .filter(po -> stoplinedto.getLines().contains(po.getName())
//                            && ListUtils.intersection(times,po.getTimetable().get(stoplinedto.getMap().get(po.getName()))).size()!=0)
//                    .collect(Collectors.toList());
//        return timetablepos;
//    }

    @Override
    public List<LineDTO> getTime(List<LineDTO> route){
        route.forEach(r ->{
            Criteria criteria = Criteria.where("name").is(r.getLinename());
            TimeTablePO result = mongoTemplate.findOne(Query.query(criteria), TimeTablePO.class, COLLECTION);
            try {
                int time = getTime(result.getTimetable().get(0).get(r.getEnd()),result.getTimetable().get(0).get(r.getStart()));
                r.setTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return route;
    }

    public int getTime(String str1, String str2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date1 = sdf.parse(str1);
        Date date2 = sdf.parse(str2);
        int minute1 = (int)date1.getTime()/(1000*60);
        int minute2 = (int)date2.getTime()/(1000*60);
        if(minute1<sdf.parse("06:00").getTime()/(1000*60)
                &&minute2>sdf.parse("22:00").getTime()/(1000*60))return minute1-minute2+24*60;
        return minute1-minute2;
    }

    @Override
    public void add(String name,List<List<String>> timetable){
        TimeTablePO timeTablePO = new TimeTablePO();
        timeTablePO.setName(name);
        timeTablePO.setTimetable(timetable);
        mongoTemplate.insert(timeTablePO);
    }
}
