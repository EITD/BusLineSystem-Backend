package com.demo.buslinesystem.bean.dto;

import com.demo.buslinesystem.bean.po.RoutePO;
import com.demo.buslinesystem.bean.po.TimeTablePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineTimeDTO {

    private RoutePO route;

    private Integer time;
}
