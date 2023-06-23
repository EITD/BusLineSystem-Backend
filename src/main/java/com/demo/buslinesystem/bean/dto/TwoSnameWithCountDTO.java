package com.demo.buslinesystem.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoSnameWithCountDTO {
    private String station1;
    private String id1;
    private String station2;
    private String id2;
    private Integer count;

}
