package com.demo.buslinesystem.bean.po;

import java.util.List;
import java.util.Objects;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("routes")
public class RoutePO {
    @Field("_id")
    private String id;

    @Field("alongStation")
    private List<String> alongStation;

    @Field("name")
    private String name;

    @Field("alongStationInChinese")
    private List<String> alongStationInChinese;

    @Field("onewayTime")
    private Integer onewayTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutePO routePO = (RoutePO) o;
        return alongStation.equals(routePO.alongStation) && name.equals(routePO.name) && onewayTime.equals(routePO.onewayTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alongStation, name, onewayTime);
    }
}
