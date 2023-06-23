package com.demo.buslinesystem.bean.dto;

import com.demo.buslinesystem.bean.po.LinePO;
import com.demo.buslinesystem.bean.po.RoutePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LTDTO {
    private LinePO route;

    private Integer time;

}
