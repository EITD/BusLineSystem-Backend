package com.demo.buslinesystem.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/12/5 10:27 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdjacentStationDTO {

    private String id1;

    private String station1;

    private String id2;

    private String station2;

    private String routeName;

    private Integer count;
}
