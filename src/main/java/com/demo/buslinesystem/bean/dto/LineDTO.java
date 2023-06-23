package com.demo.buslinesystem.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineDTO {
    private String linename;
    private List<String> alongwaystation;
    private String id;
    private String stationname;
    private String english;
    private int start;
    private int end;
    private int time;
}
