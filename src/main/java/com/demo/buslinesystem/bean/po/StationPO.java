package com.demo.buslinesystem.bean.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("stations")
public class StationPO {

    @Field("_id")
    private String _id;

    @Field("english")
    private String english;

    @Field("id")
    private String id;

    @Field("name")
    private String name;
}
