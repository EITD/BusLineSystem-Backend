package com.demo.buslinesystem.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationGroupDTO {
    private String name;

    private Integer num;

    private Set<String> stations;
}
