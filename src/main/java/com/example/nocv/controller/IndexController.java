package com.example.nocv.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.nocv.entity.ChinaTotal;
import com.example.nocv.entity.LineTrend;
import com.example.nocv.entity.NocvData;
import com.example.nocv.entity.NocvNews;
import com.example.nocv.service.ChinaTotalService;
import com.example.nocv.service.IndexService;
import com.example.nocv.service.NocvNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private ChinaTotalService chinaTotalService;

    @Autowired
    private NocvNewsService nocvNewsService;


    @RequestMapping("/")
    public String index(Model model){

        //找到id最大的数据
        Integer id = chinaTotalService.maxID();
        //根据id进行查询
        ChinaTotal chinaTotal = chinaTotalService.getById(id);
        model.addAttribute("chinaTotal",chinaTotal);

        return "index";
    }

    /*
    * China地图页面
    * */
    @RequestMapping("/toChina")
    public String toChina(Model model) throws ParseException {

        //找到id最大的数据
        Integer id = chinaTotalService.maxID();
        //根据id进行查询

        //redis查询数据库逻辑
        /*
        *
        * 1.先查询redis缓存，【有数据直接返回】【没有数据，查询mysql数据库，更新缓存，返回客户端】
        * */

        Jedis jedis = new Jedis("192.168.157.128");

        //拿到客户端连接【判断有没有使用redis】
        if (jedis!=null){
            String confirm = jedis.get("confirm");
            String input = jedis.get("input");
            String heal = jedis.get("heal");
            String dead = jedis.get("dead");
            String updateTime = jedis.get("updateTime");
            if (StringUtils.isNotBlank(confirm)
                    && StringUtils.isNotBlank(input)
                    && StringUtils.isNotBlank(heal)
                    && StringUtils.isNotBlank(dead)
                    && StringUtils.isNotBlank(updateTime)){
                ChinaTotal chinaTotalRedis = new ChinaTotal();
                chinaTotalRedis.setConfirm(Integer.parseInt(confirm));
                chinaTotalRedis.setInput(Integer.parseInt(input));
                chinaTotalRedis.setHeal(Integer.parseInt(heal));
                chinaTotalRedis.setDead(Integer.parseInt(dead));
                //格式调整String---》Date
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(updateTime);
                chinaTotalRedis.setUpdateTime(date);

                model.addAttribute("chinaTotal",chinaTotalRedis);
                //3.疫情播报新闻
                List<NocvNews> newsList = nocvNewsService.listNewsLimit5();
                model.addAttribute("newsList",newsList);
                return "china";
            }else {
                //缓存里面没有数据
                ChinaTotal chinaTotal = chinaTotalService.getById(id);
                model.addAttribute("chinaTotal",chinaTotal);
                //3.疫情播报新闻
                List<NocvNews> newsList = nocvNewsService.listNewsLimit5();
                model.addAttribute("newsList",newsList);
                //更新缓存

                jedis.set("confirm",String.valueOf(chinaTotal.getConfirm()));
                jedis.set("input",String.valueOf(chinaTotal.getId()));
                jedis.set("heal",String.valueOf(chinaTotal.getHeal()));
                jedis.set("dead",String.valueOf(chinaTotal.getDead()));

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(chinaTotal.getUpdateTime());

                jedis.set("updateTime",date);

                return "china";
            }
        }

        return "china";
    }

    @RequestMapping("/query")
    @ResponseBody
    public List<NocvData> queryData() throws ParseException {
        /*QueryWrapper<NocvData> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(new Date());
        Date parse = format.parse(format1);
        queryWrapper.ge("update_time",parse);*/


        //有缓存先查缓存[list]  有数据返回即可
        Jedis jedis = new Jedis("192.168.157.128");
        if (jedis!=null) {
            // 有缓存数据，返回数据即可
            List<String> listRedis = jedis.lrange("nocvdata", 0, 33);
            List<NocvData> dataList = new ArrayList<>();
            if (listRedis.size()>0) {
                for (int i = 0; i < listRedis.size(); i++) {
                    System.out.println("列表项为："+listRedis.get(i));
                    String s = listRedis.get(i);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    Object name = jsonObject.get("name");
                    Object value = jsonObject.get("value");
                    NocvData nocvData = new NocvData();
                    nocvData.setName(String.valueOf(name));
                    nocvData.setValue(Integer.parseInt(value.toString()));
                    dataList.add(nocvData);

                }
                //查询redis缓存数据库 返回的数据
                return dataList;
            }else {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //redis没有数据 查数据库
                List<NocvData> list = indexService.listOrderByIdLimit34();
                for (NocvData nocvData : list) {
                    jedis.lpush("nocvdata", JSONObject.toJSONString(nocvData));

                }
                return list;
            }
        }
        //默认没有连接redis的返回数据库
        //redis没有数据 查数据库
        List<NocvData> list = indexService.listOrderByIdLimit34();
        return list;
    }

    //跳转pie页面
    @RequestMapping("/toPie")
    public String toPie(){
        return "pie";
    }

    /*
    * 分组聚合
    * select count(*) from goods group by type;
    * */

    @RequestMapping("/queryPie")
    @ResponseBody
    public List<NocvData> queryPieData(){
        List<NocvData> list = indexService.listOrderByIdLimit34();
        return list;
    }

    /*
    * 跳转柱状图页面
    * */

    @RequestMapping("/toBar")
    public String toBar(){
        return "bar";
    }

    @RequestMapping("/queryBar")
    @ResponseBody
    public Map<String,List<Object>> queryBarData(){
        //1.所有城市数据:数值
        List<NocvData> list = indexService.listOrderByIdLimit34();

        //2.所有的城市数据
        List<String> cityList = new ArrayList<>();
        for (NocvData s :list) {
            cityList.add(s.getName());
        }

        //3.所有疫情数值数据
        List<Integer> dataList = new ArrayList<>();
        for (NocvData s :list) {
            dataList.add(s.getValue());
        }

        //4.创建map
        Map map = new HashMap();
        map.put("cityList",cityList);
        map.put("dataList",dataList);


        return map;
    }

    @RequestMapping("/toLine")
    public String toLine(){
        return "line";
    }

    @RequestMapping("/queryLine")
    @ResponseBody
    public Map<String,List<Object>> queryLineData(){
        //1.查询近七天的数据
        List<LineTrend> list7Day = indexService.findSevenData();

        //2.封装所有的确诊人数
        List<Integer> confirmList = new ArrayList<>();

        //3.封装所有的隔离人数
        List<Integer> isolation = new ArrayList<>();

        //4.封装所有的治愈人数
        List<Integer> cureList = new ArrayList<>();

        //5.封装所有的死亡人数
        List<Integer> deadList = new ArrayList<>();

        //6.封装所有的疑似人数
        List<Integer> similarList = new ArrayList<>();
        for(LineTrend data:list7Day){
            confirmList.add(data.getConfirm());
            isolation.add(data.getIsolation());
            similarList.add(data.getSimilar());
            cureList.add(data.getCure());
            deadList.add(data.getDead());
        }

        //7.返回时的格式容器Map
        Map map = new HashMap();
        map.put("confirmList",confirmList);
        map.put("isolation",isolation);
        map.put("cureList",cureList);
        map.put("deadList",deadList);
        map.put("similarList",similarList);

        return map;

    }
}
