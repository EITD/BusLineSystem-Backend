package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.dto.findLinesDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.dto.LTDTO;
import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.StationPO;
import com.demo.buslinesystem.common.LineField;
import com.demo.buslinesystem.dao.LineDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Slf4j
@Repository
@Service
public class LineDaoImpl implements LineDao {

    private final static String COLLECTION = LineField.COLLECTION;

    @Resource(name = "mongoTemplate")
    private MongoOperations mongoOperations;

    @Override
    public String getNameById(String line) {
        Criteria criteria = Criteria.where("id").is(line);

        LinePO po = mongoOperations.findOne(Query.query(criteria), LinePO.class, COLLECTION);

        return po.getName();
    }

    @Override
    public Object[] selectLines() {
        List<findLinesDTO> result = new ArrayList<>();
        List<LinePO> linePOS = mongoOperations.findAll(LinePO.class);
        linePOS.forEach(r->{
            result.add(findLinesDTO.builder().value(r.getName()).build());
        });
        Object[] objects = result.toArray();
        return objects;
    }


    @Override
    public List<LTDTO> sortedByT(Integer option, Integer num) {
        List<LinePO> allLines = mongoOperations.findAll(LinePO.class, "lines");
        List<LTDTO> result = new ArrayList<>();
        if (option == 0) {
            result = allLines.stream()
                    .limit(num)
                    .map(e -> LTDTO.builder().route(e).time(toMinute(e.getRuntime(), e.getInterval(), e.getOnewayTime())).build())

                    .collect(Collectors.toList());
        }

        if (option == 1) {
            result = allLines.stream()
                    .sorted((a, b) ->
                            toMinute(a.getRuntime(), a.getInterval(), a.getOnewayTime())-toMinute(b.getRuntime(), b.getInterval(), b.getOnewayTime())
                    )
                    .limit(num)
                    .map(e -> LTDTO.builder().route(e).time(toMinute(e.getRuntime(), e.getInterval(), e.getOnewayTime())).build())
                    .collect(Collectors.toList());
        }
        if (option == 2) {
            result = allLines.stream()
                    .sorted((a, b) ->
                            toMinute(b.getRuntime(), b.getInterval(), b.getOnewayTime())-toMinute(a.getRuntime(), a.getInterval(), a.getOnewayTime()))
                    .limit(num)
                    .map(e -> LTDTO.builder().route(e).time(toMinute(e.getRuntime(), e.getInterval(), e.getOnewayTime())).build())
                    .collect(Collectors.toList());
        }


        return result;
    }

    private Integer toMinute(String runtime,Integer interval,String onewaytime){
         String[] times=runtime.split("[-, :]");

         Integer num=Integer.parseInt(times[2]) * 60 + Integer.parseInt(times[3])
                        - Integer.parseInt(times[0]) * 60 - Integer.parseInt(times[1])
                        / interval * Integer.parseInt(onewaytime.substring(1, 2));
         return num;


    }

    @Override
    public List<LinePO> getLineInfo(String line) {
        Criteria criteria = Criteria.where(LineField.NAME).is(line);
        LinePO result = mongoOperations.findOne(Query.query(criteria), LinePO.class, COLLECTION);
//        if(result.isDirectional())
        List<LinePO> list = new ArrayList<LinePO>();
        list.add(result);
        return list;
    }

    @Override
    public List<Integer> lineCountByGroup() {
        List<Integer> result = new ArrayList<>(8);
        result.add(0);result.add(0);result.add(0);result.add(0);
        result.add(0);result.add(0);result.add(0);result.add(0);
        List<LinePO> allLines = mongoOperations.findAll(LinePO.class, "lines");

        allLines.stream().forEach(line->{
            if(line.getName().charAt(0)=='K'){
                result.set(5, result.get(5) + 1);
            }
            else if(line.getName().charAt(0)=='G'){
                result.set(6, result.get(6) + 1);
            }
            else if(line.getName().charAt(0)=='N'){
                result.set(7, result.get(7) + 1);
            }
            else{
                if(line.getType().equals("干线"))
                    result.set(0, result.get(0) + 1);
                else if(line.getType().equals("支线"))
                    result.set(1, result.get(1) + 1);
                else if(line.getType().equals("城乡线"))
                    result.set(2, result.get(2) + 1);
                else if(line.getType().equals("驳接线"))
                    result.set(3, result.get(3) + 1);
                else if(line.getType().equals("社区线"))
                    result.set(4, result.get(4) + 1);
            }
        });
        return result;

    }

    @Override
    public List<String> directStationCount(String line) {
        Criteria criteria = Criteria.where(LineField.NAME).is(line);
        LinePO linepo = mongoOperations.findOne(Query.query(criteria), LinePO.class, "lines");
        if(!linepo.isDirectional()){return null;}
        List<RoutePO> routePOS = mongoOperations.findAll(RoutePO.class,"routes");
        List<RoutePO> routePOTwo = new ArrayList<>();
        List<String> result = new ArrayList<>();

        routePOS.forEach(r->{
            if(r.getName().contains(linepo.getName())){
                    routePOTwo.add(r);
            }
        });

        if(routePOTwo.size()==2){
                RoutePO temp =new RoutePO();
                temp = routePOTwo.get(0);
                routePOTwo.get(0).getAlongStation().removeAll(routePOTwo.get(1).getAlongStation());
                routePOTwo.get(1).getAlongStation().removeAll(temp.getAlongStation());
                routePOTwo.get(0).getAlongStation().addAll(routePOTwo.get(1).getAlongStation());
                routePOTwo.get(0).getAlongStation().forEach(s->{
                    result.add(s);
                });
        }
        else {
                return null;
        }

        return result;
    }

//    @Override
//    public List<LineDTO> getTime(List<LineDTO> route){
//        route.forEach(r ->{
//            String str1 = "路",str2 = "路上行",str3 = "路下行";
//            String str = r.getName().replace(str2,"").replace(str3,"").replace(str1,"");
//            Criteria criteria = Criteria.where(LineField.NAME).is(str);
//            LinePO result = mongoOperations.findOne(Query.query(criteria), LinePO.class, COLLECTION);
//            r.setTime(result.getInterval()*(r.getAlongwaystation().size()+1));
//        });
//        return route;
//    }
}
