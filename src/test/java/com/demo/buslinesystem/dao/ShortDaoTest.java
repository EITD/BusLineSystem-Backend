package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

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
class ShortDaoTest {
    @Resource
    private ShortDao shortDao;

    @Test
    void findShort() {
        String id1 = "16115";
        String id2 = "14768";
        List<String> result = shortDao.findShort(id1,id2);
        log.info(result.toString());
    }

//    @Test
//    void getTime(){
//        LineDTO linedto = new LineDTO("10路上行", Lists.list("62765", "62737", "62729", "6354", "6363"),0);
//        List<LineDTO> list = new ArrayList<>();
//        list.add(linedto);
//        log.info(lineDao.getTime(list).toString());
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
