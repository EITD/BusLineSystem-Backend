package com.demo.buslinesystem.bean.dto;

import com.demo.buslinesystem.bean.po.StationPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationStatisticsDTO {

    private StationPO station;

    private List<String> route_lists;

    private Integer count;


}
