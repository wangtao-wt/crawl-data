package com.wt.crawl.demo;

import com.wt.crawl.http.HttpPageDownload;
import com.wt.crawl.pojo.HouseInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



/**
 * @author wangtao
 */
public class GetHouseInfos {

    public static void main(String[] args) throws IllegalAccessException {
        String url = "https://hf.ke.com/ershoufang/feixi/";
        String s = "";
        //遍历一次是一页的数据
        for (int i = 1; i < 50; i++) {
            if (i == 1) {
                s = HttpPageDownload.sendGet(url + "de3");
            } else {
                s += HttpPageDownload.sendGet(url + "pg" + i + "de3");
            }
        }
        Document document = Jsoup.parse(s);
        Elements elements = document.getElementsByClass("info clear");

        List<HouseInfo> infos = new ArrayList<>();
        for (Element element : elements) {
            HouseInfo info = new HouseInfo();
            Elements positionInfo = element.getElementsByClass("positionInfo");

            String name = positionInfo.select("a").first().text();
            info.setName(name);

            String houseIcon = element.getElementsByClass("houseInfo").text();
            String[] split = houseIcon.split("\\|");
            if (split.length == 3) {
                info.setHouseType(split[0]);
                info.setArea(split[1]);
                info.setPosition(split[2]);
            } else {
                info.setHouseType(split[0] + split[2]);
                info.setYears(split[1]);
                info.setArea(split[3]);
                info.setPosition(split[4]);
            }

            String unitPrice = element.getElementsByClass("unitPrice").select("span").first().text();
            info.setUnitPrice(unitPrice);

            String totalPrice = element.getElementsByClass("totalPrice").select("span").first().text();
            info.setTotalPrice(totalPrice);
            infos.add(info);
        }
        for (HouseInfo info : infos) {
            System.out.println(info);
        }

        writeExcel(infos);
    }


    public static void writeExcel(List<HouseInfo> infos) throws IllegalAccessException {
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("房产信息");

        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);

        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("小区");

        cell = row.createCell(1);
        cell.setCellValue("户型");

        cell = row.createCell(2);
        cell.setCellValue("面积");

        cell = row.createCell(3);
        cell.setCellValue("方位");

        cell = row.createCell(4);
        cell.setCellValue("单价");

        cell = row.createCell(5);
        cell.setCellValue("总价");

        cell = row.createCell(6);
        cell.setCellValue("年代");

        //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值

        List<HouseInfo> list = infos.stream().filter(info -> Double.parseDouble(info.getTotalPrice()) >= 100 && Double.parseDouble(info.getTotalPrice()) <= 200).collect(Collectors.toList());
        for (int i = 1; i <= list.size(); i++) {
            HouseInfo info = list.get(i-1);

            row = sheet.createRow(i);

            Field[] declaredFields = info.getClass().getDeclaredFields();
            for (int j = 0; j < declaredFields.length; j++) {
                cell = row.createCell(j);
                declaredFields[j].setAccessible(true);
                cell.setCellValue((String) declaredFields[j].get(info));
            }
        }
        File file = new File("D:/feixi.xls");
        if (file.exists()) {
            file.delete();
        }
        //将文件保存到指定的位置
        try {
            file.createNewFile();
            workbook.write(file);
            System.out.println("写入成功");
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
