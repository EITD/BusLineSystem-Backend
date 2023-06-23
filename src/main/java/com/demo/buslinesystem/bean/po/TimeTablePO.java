package com.demo.buslinesystem.bean.po;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document("timetables")
public class TimeTablePO {
    @Field("_id")
    private String id;

    @Field("timetable")
    private List<List<String>> timetable;

    @Field("name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTablePO that = (TimeTablePO) o;
        return Objects.equals(timetable, that.timetable) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timetable, name);
    }
}
