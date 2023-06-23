package com.demo.buslinesystem.dao;

import com.demo.buslinesystem.bean.dto.LineDTO;
import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.dto.LTDTO;

import java.util.List;


public interface LineDao {

    List<LTDTO> sortedByT(Integer option, Integer num) ;

    List<LinePO> getLineInfo(String line);

    List<Integer> lineCountByGroup();

    List<String> directStationCount(String line);

    String getNameById(String line);

    Object[] selectLines();


    //List<LineDTO> getTime(List<LineDTO> route);
}
