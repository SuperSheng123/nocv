package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.NocvData;
import com.example.nocv.service.IndexService;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.NocvDataVo;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
public class ChinaDataAdminController {

    @Autowired
    private IndexService indexService;



    /*
     * 模糊查询 带有分页
     * */
    @GetMapping ("/listDataByPage")
    public DataView listDataByPage(NocvDataVo nocvDataVo) {
        //1.创建分页的对象 当前页 每页限制大小
        IPage<NocvData> page = new Page<>(nocvDataVo.getPage(), nocvDataVo.getLimit());

        //2.创建模糊查询的条件
        QueryWrapper<NocvData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!(nocvDataVo.getName() == null), "name", nocvDataVo.getName());
        //3.疫情数据确诊最多的排在最上面
        queryWrapper.orderByDesc("value");
        //4.查询数据库
        indexService.page(page, queryWrapper);
        //5.返回数据封装
        DataView dataView = new DataView(page.getTotal(), page.getRecords());

        return dataView;
    }


    @DeleteMapping("/china/deleteById")
    public DataView deleteById(Integer id) {
        indexService.removeById(id);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("删除中国地图数据成功！");
        return dataView;
    }


    /*
     * 新增或者修改
     * id：nocvData 有值修改，没有值新增
     * */
    @PostMapping("/china/addOrUpdateChina")
    public DataView addChina(NocvData nocvData) {


        boolean save = indexService.saveOrUpdate(nocvData);
        DataView dataView = new DataView();

        if (save) {
            dataView.setCode(200);
            dataView.setMsg("操作成功！");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("操作失败！");
        return dataView;
    }

    @RequestMapping("/china/deleteByIds")

    public DataView deleteByIds(@RequestParam(name = "ids[]") Integer[] ids) {


        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            boolean i1 = indexService.removeById(ids[i]);
            if (i1) {
                ++count;
            }
        }

        DataView dataView = new DataView();

        if (count == ids.length) {
            dataView.setCode(200);
            dataView.setMsg("全部删除成功");
            return dataView;
        }
        dataView.setMsg("删除出现异常");
        dataView.setCode(100);
        return dataView;
    }

    /*
    *
    * Excel的拖拽或者点击上传
    * 1.前台页面发送一个请求，上传文件MutilpartFile Http
    * 2.Controller,上传文件MutilpartFile 参数
    * 3.POI解析文件，里面的数据一行一行解析出来
    * 4.每一条数据插入数据库
    * */

    @RequestMapping("/excelImportChinaData")

    public DataView excelImportChinaData(@RequestParam("file")MultipartFile file) throws IOException {

        DataView dataView =new DataView();
        //1.文件不能为空
        if (file.isEmpty()){
            dataView.setMsg("文件为空，不能上传");
        }

        //2.POI获取excel解析数据
        XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);

        //3.定义一个集合 接收文件中的数据
        List<NocvData> list =new ArrayList<>();


        XSSFRow row = null;
        //4.解析数据，装到集合里面
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            //定义实体
            NocvData nocvData = new NocvData();

            //每一行的数据放到实体类里面
            row = sheet.getRow(i);
            //解析数据
            nocvData.setName(row.getCell(0).getStringCellValue());
            nocvData.setValue((int) row.getCell(1).getNumericCellValue());

            //5.添加list集合
            list.add(nocvData);
        }

        System.out.println(list);
        //6.插入数据库
        indexService.saveBatch(list);
        dataView.setCode(200);
        dataView.setMsg("excel已经插入成功");

        return dataView;
    }

    /*
    * 到处excel数据 中国疫情数据
    * 1.先去查询数据库
    * 2.建立excel对象，封装数据
    * 3.建立数据流，输出文件
    * */

    @RequestMapping("/excelOutPortChina")

    public void excelOutPortChina(String[] id, HttpServletResponse httpServletResponse) throws IOException {

        List<NocvData> list = new ArrayList<>();
        Collection<NocvData> nocvDataList = indexService.listByIds(Arrays.asList(id));
        //建立Excel对象，封装数据
        httpServletResponse.setCharacterEncoding("UTF-8");
        XSSFWorkbook xw = new XSSFWorkbook();
        //创建sheet对象
        XSSFSheet sheet = xw.createSheet("中国疫情数据sheet1");
        //创建表头标题
        XSSFRow xssfRow = sheet.createRow(0);
        xssfRow.createCell(0).setCellValue("城市名称");
        xssfRow.createCell(1).setCellValue("确诊数量");

        //遍历数据，封装Excel对象
        for (NocvData data:nocvDataList){
            XSSFRow dataRow =sheet.createRow(sheet.getLastRowNum()+1);
            dataRow.createCell(0).setCellValue(data.getName());
            dataRow.createCell(1).setCellValue(data.getValue());
        }

        OutputStream os = null;

        try {
            httpServletResponse.setContentType("application/octet-stream;charset=utf8");

            //建立输出流，输出浏览器文件
             os = httpServletResponse.getOutputStream();

            httpServletResponse.addHeader("Content-Disposition", "attachment;filename=nocvList.xlsx");

            //设置excel名称
            xw.write(os);//此方法能直接将内存中的数据放到输出流中，与上面的方法相比，大大的提高了效率
            os.flush();
        }catch (Exception e){
           e.printStackTrace();
        }finally {
            if (os!=null){
                os.close();
            }
        }


    }
}
