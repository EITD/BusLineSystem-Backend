package com.demo.buslinesystem.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StopLineDTO {
    String name;
    int index;
//    List<String> lines;
//    Map<String,Integer> map;
    String lineTime;
}
