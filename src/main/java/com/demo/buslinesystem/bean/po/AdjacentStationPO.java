package com.demo.buslinesystem.bean.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @Description
 * @Author wuhongbin sushuduo houyining
 * @Date 2021/12/5 5:25 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("adjacent_station")
public class AdjacentStationPO {

    @Id
    private String _id;

    @Field("id1")
    private String id1;

    @Field("station1")
    private String station1;

    @Field("id2")
    private String id2;

    @Field("station2")
    private String station2;

    @Field("route_name")
    private String routeName;

    @Field("count")
    private Integer count;
}
