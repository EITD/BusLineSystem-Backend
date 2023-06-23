package com.demo.buslinesystem.bean.dto;

import com.demo.buslinesystem.bean.po.RoutePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S1L1DTO {

    private  String name;

    private List<String> routes;
}

