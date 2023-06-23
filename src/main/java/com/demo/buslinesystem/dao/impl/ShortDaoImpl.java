package com.demo.buslinesystem.dao.impl;

import com.demo.buslinesystem.bean.po.ShortPO;
import com.demo.buslinesystem.dao.ShortDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShortDaoImpl implements ShortDao {
    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public void saveBatch(List<ShortPO> shortPOS) {
        mongoTemplate.insertAll(shortPOS);
    }

    public List<String> nextContains(List<List<String>> list,String id2){
        for(int i=0;i<list.size();i++){
            if(list.get(i).contains(id2))
                return list.get(i).subList(0,list.get(i).indexOf(id2)+1);
        }
        return null;
    }
    @Override
    public List<String> findShort(String id1,String id2){
        List<ShortPO> shortPOS = mongoTemplate.findAll(ShortPO.class);
        Map<String,List<List<String>>> map = new HashMap<>();
        shortPOS.forEach(shortpo->map.put(shortpo.getId(),shortpo.getNext()));
        if(map.get(id1)==null||map.get(id2)==null)return null;
        List<List<String>> result = new ArrayList<>();
        List<List<String>> list = map.get(id1);
        List<String> temp = nextContains(list,id2);
        if(temp!=null)result.add(temp); 
        else { 
            for(int i1=0;i1<list.size();i1++){
                for(int j1=1;j1<list.get(i1).size();j1++){
                    List<List<String>> list1 = map.get(list.get(i1).get(j1));
                    List<String> front1 = list.get(i1).subList(0, j1);
                    List<String> back1 = nextContains(list1,id2);
                    if(back1!=null) {
                        result.add(new ArrayList<String>(){{
                            addAll(front1);
                            addAll(back1);
                        }}); 
                    }
                    else{
                        for(int i2=0;i2<list1.size();i2++) {
                            for (int j2 = 1; j2 < list1.get(i2).size(); j2++) {
                                List<List<String>> list2 = map.get(list1.get(i2).get(j2));
                                List<String> front2 = list1.get(i2).subList(0, j2);
                                List<String> back2 = nextContains(list2,id2);
                                if(back2!=null){
                                    result.add(new ArrayList<String>(){{
                                        addAll(front1);
                                        addAll(front2);
                                        addAll(back2);
                                    }}); 
                                }
                            }
                        }
                    }
                }
            }
        }
        if(result==null)return null;
        AtomicReference<List<String>> res = new AtomicReference<>(result.get(0));
        AtomicInteger min = new AtomicInteger(result.get(0).size());
        result.forEach(r->{
            if(r.size()< min.get()){
                res.set(r);
                min.set(r.size());
            }
        });
        return res.get();
    }
}
