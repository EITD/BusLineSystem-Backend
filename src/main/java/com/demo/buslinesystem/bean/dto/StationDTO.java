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
public class StationDTO {
    private String linename;
    private String id;
    private String stationname;
    private String english;
}
