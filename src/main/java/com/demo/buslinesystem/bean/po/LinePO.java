package com.demo.buslinesystem.bean.po;

import com.demo.buslinesystem.common.LineField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(LineField.COLLECTION)
public class LinePO {

    @Field(LineField.ID)
    private ObjectId id;

    @Field(LineField.DIRECTIONAL)
    private boolean directional;

    @Field(LineField.INTERVAL)
    private Integer interval;

    @Field(LineField.KILOMETER)
    private Integer kilometer;

    @Field(LineField.NAME)
    private String name;

    @Field(LineField.ONEWAYTIME)
    private String onewayTime;

    @Field(LineField.ROUTE)
    private String route;

    @Field(LineField.RUNTIME)
    private String runtime;

    @Field(LineField.TYPE)
    private String type;
}
