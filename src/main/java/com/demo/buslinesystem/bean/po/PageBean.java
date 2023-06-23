package com.demo.buslinesystem.bean.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T> {
    private int total;
    private int totalPage;
    private List<T> list;
    private int pagenum;
    private int pagesize;
}
