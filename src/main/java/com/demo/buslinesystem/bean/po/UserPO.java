package com.demo.buslinesystem.bean.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class UserPO {

    @Id
    private String id;

    @Field("station1")
    private String station1;

    @Field("id1")
    private String id1;

    @Field("station2")
    private String station2;

    @Field("id2")
    private String id2;

    @Field("num")
    private Integer num;

    @Field("route_name")
    private String routeName;
}
