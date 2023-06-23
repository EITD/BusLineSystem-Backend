package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.LatestDTO;
import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.dto.StopLineDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.po.TimeTablePO;
import com.demo.buslinesystem.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//package com.demo.buslinesystem.dao;
//
//import com.demo.buslinesystem.bean.po.LinePO;
//import com.demo.buslinesystem.service.LineService;
//import javax.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/10/14 10:40 下午
 */
@Slf4j
@SpringBootTest
class TimeTableDaoTest {

    @Resource
    private TimeTableDao timetableDao;

//    @Test
//    void getStopLine(){
//        List<String> times = new ArrayList<>();
//        times.add("08:37");times.add("08:38");times.add("08:39");times.add("08:40");times.add("08:41");times.add("08:42");times.add("08:43");times.add("08:44");times.add("08:45");times.add("08:46");times.add("08:47");
//        List<StopLineDTO> stoplinedtos = new ArrayList<>();
//        Map<String,Integer> map = new HashMap<>();
//        map.put("53路下行",8);map.put("70路上行",13);map.put("N11路下行",15);map.put("174路下行",5);map.put("101路",8);
//        StopLineDTO stoplinedto = new StopLineDTO("16147",Lists.list("101路","174路下行","53路下行","70路上行","N11路下行"),map,null);
//        stoplinedtos.add(stoplinedto);
//        List<StopLineDTO> result = timetableDao.getStopLine(stoplinedtos,times);
//        log.info(result.toString());
//    }

//    @Test
//    void test(){
//        List<String> times = new ArrayList<>();
//        times.add("08:37");times.add("08:38");times.add("08:39");times.add("08:40");times.add("08:41");times.add("08:42");times.add("08:43");times.add("08:44");times.add("08:45");times.add("08:46");times.add("08:47");
//        Map<String,Integer> map = new HashMap<>();
//        map.put("53路下行",8);map.put("70路上行",13);map.put("N11路下行",15);map.put("174路下行",5);map.put("101路",8);
//        StopLineDTO stoplinedto = new StopLineDTO("16147",Lists.list("101路","174路下行","53路下行","70路上行","N11路下行"),map,null);
//        List<TimeTablePO> result = timetableDao.test(stoplinedto,times);
//        log.info(result.size()+"");
//    }

//    @Test
//    void getTime() throws ParseException {
//        log.info(timetableDao.getTime("00:07","23:58")+"");
//    }

    @Test
    void getLatest(){
        List<LatestDTO> list = new ArrayList<>();
        LatestDTO latestdto1 = new LatestDTO(null,null,null,"82路下行",12);
        LatestDTO latestdto2 = new LatestDTO(null,null,null,"82路上行",20);
        list.add(latestdto1);
        list.add(latestdto2);
        List<LatestDTO> result = timetableDao.getLatest(list,"10:32");
        log.info(result.toString());
    }

//    @Test
//    void getTime(){
//        LineDTO linedto = new LineDTO("10路上行", Lists.list("62765", "62737", "62729", "6354", "6363"),8,14,0);
//        List<LineDTO> list = new ArrayList<>();
//        list.add(linedto);
//        log.info(timetableDao.getTime(list).toString());
//    }
}

//@Slf4j
//@SpringBootTest
//class LineDaoTest {
//
//    @Resource
//    private LineService lineService;
//
//    @Test
//    void getLineInfo() {
//        String name = "N8";
//        LinePO lineInfo = lineService.getLineInfo(name);
//        log.info(lineInfo.toString());
//    }
//}
