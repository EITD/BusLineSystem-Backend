package com.demo.buslinesystem.service;

import com.demo.buslinesystem.bean.po.PageBean;
import com.demo.buslinesystem.bean.po.RoutePO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Slf4j
@SpringBootTest
class LineServiceTest {

    @Resource
    private LineService lineService;

    @Test
    void getTimeList() throws ParseException {
        List<String> times = lineService.getTimeList("08:37","10");
        log.info(times.toString());
    }

}
