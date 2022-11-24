package com.example.nocv.tengxunapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.nocv.entity.ChinaTotal;
import com.example.nocv.entity.NocvData;
import com.example.nocv.service.ChinaTotalService;
import com.example.nocv.service.IndexService;
import com.mysql.fabric.proto.xmlrpc.InternalXmlRpcMethodCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChinaTotalScheduleTask {


    @Autowired
    private ChinaTotalService chinaTotalService;

    @Autowired
    private IndexService indexService;


    /*
     * 每小时更新执行一次，更新全国数据情况
     *
     * */
    //@Scheduled(fixedDelay = 10000) //10s执行一次
    //@Scheduled(cron = "0 0 2 * * ?")
    public void updateChinaTotalToDB() throws ParseException {
        HttpUtils httpUtils = new HttpUtils();
        String string = httpUtils.getData();
//        System.out.println("数据："+string);

        //1.所有数据的alibaba格式
        JSONObject jsonObject = JSONObject.parseObject(string);
        Object data = jsonObject.get("data");


        //2.data
        JSONObject jsonObjectData = JSONObject.parseObject(data.toString());
        Object chinaTotal = jsonObjectData.get("chinaTotal");
        Object lastUpdateTime = jsonObjectData.get("overseaLastUpdateTime");


        //total全中国整体疫情数据
        JSONObject jsonObjectTotal = JSONObject.parseObject(chinaTotal.toString());
        Object total = jsonObjectTotal.get("total");


        //total全中国整体疫情数据
        JSONObject jsonObjectTotalConfirm = JSONObject.parseObject(total.toString());
        Object confirm = jsonObjectTotalConfirm.get("confirm");
        Object input = jsonObjectTotalConfirm.get("input");
        Object severe = jsonObjectTotalConfirm.get("severe");
        Object heal = jsonObjectTotalConfirm.get("heal");
        Object dead = jsonObjectTotalConfirm.get("dead");
        Object suspect = jsonObjectTotalConfirm.get("suspect");

        ChinaTotal dataEntity = new ChinaTotal();
        dataEntity.setConfirm((Integer) confirm);
        dataEntity.setHeal((Integer) heal);
        dataEntity.setDead((Integer) dead);
        dataEntity.setInput((Integer) input);
        dataEntity.setSevere((Integer) severe);
        dataEntity.setSuspect((Integer) suspect);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dataEntity.setUpdateTime((format.parse(String.valueOf(lastUpdateTime))));

        //插入数据库
        chinaTotalService.save(dataEntity);



        /*
         * 中国各个省份数据的自动刷新
         *
         * */
        //拿到areaTree
        JSONArray areaTree = jsonObjectData.getJSONArray("areaTree");
        Object[] objects = areaTree.toArray();

        //遍历所有国家的数据
        /*for (int i = 0; i < objects.length; i++) {
            JSONObject jsonObject1 = JSONObject.parseObject(objects[i].toString());
            Object name = jsonObject1.get("name");
            System.out.println(name);
        }*/

        //拿到中国的数据
        JSONObject jsonObject1 = JSONObject.parseObject(objects[2].toString());

        JSONArray children = jsonObject1.getJSONArray("children");

        Object[] objects1 = children.toArray();//各个省份的数据

        List<NocvData> list = new ArrayList<>();

        for (int i = 0; i < objects1.length; i++){
            NocvData nocvData = new NocvData();
            JSONObject jsonObject2 = JSONObject.parseObject(objects1[i].toString());
            Object name = jsonObject2.get("name");//省份名字

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Object lastUpdateTimePro = jsonObject2.get("lastUpdateTime");//省份更新数据时间
            Object total1 = jsonObject2.get("total");
            JSONObject jsonObject3 = JSONObject.parseObject(total1.toString());//total数据
            Object confirm1 = jsonObject3.get("confirm");//累计确诊人数

            //获取累计死亡人数 和 治愈人数
            Object heal1 = jsonObject3.get("heal");
            Object dead1 = jsonObject3.get("dead");

            //计算现存确诊
            int xianConfirm = Integer.parseInt(confirm1.toString()) - Integer.parseInt(heal1.toString())  - Integer.parseInt(dead1.toString());
            //赋值
            nocvData.setName(name.toString());
            nocvData.setValue(xianConfirm);

            if (lastUpdateTimePro == null){
                nocvData.setUpdateTime(new Date());
            }else {
                nocvData.setUpdateTime(format1.parse(String.valueOf(lastUpdateTimePro)));
            }



            list.add(nocvData);

        }

        System.out.println(list);
        //插入数据库
        indexService.saveBatch(list);


        //删除缓存中的数据【非常重要】msql --- redis 一致性的一个简单套路
        Jedis jedis = new Jedis("192.168.157.128");
        if (jedis!=null) {
            jedis.flushDB();//只删除使用的就好，小心使用flushDB
        }




    }
}
