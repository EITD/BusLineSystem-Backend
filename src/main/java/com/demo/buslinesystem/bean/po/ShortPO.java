package com.demo.buslinesystem.bean.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("short")
public class ShortPO {
    @Field("id")
    private String id;

    @Field("next")
    private List<List<String>> next;

}
