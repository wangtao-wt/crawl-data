package com.wt.crawl.demo;

import com.wt.crawl.http.HttpDownloadDynamic;
import com.wt.crawl.pojo.HouseInfo;
import com.wt.crawl.pojo.JobInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetBossInfos {

    public static void main(String[] args) throws NoSuchMethodException, ScriptException, IOException, IllegalAccessException, InterruptedException {
        //https://www.zhipin.com/c101220100/?page=1&ka=page-1
        String defaultUrl = "https://www.zhipin.com/c101220100/?page=1&ka=page-1";
        String cookie = HttpDownloadDynamic.getCookie(defaultUrl);
        String s = "";
        for (int i = 1; i < 2; i++) {
            String url = "https://www.zhipin.com/c101220100/?page=" + i + "&ka=page-" + i;
            s += HttpDownloadDynamic.sendGet(url, cookie);
        }
        List<JobInfo> jobInfos = parseHtml(s);
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo);
        }
        writeExcel(jobInfos);
    }

    public static List<JobInfo> parseHtml(String s) {

        Document document = Jsoup.parse(s);
        Elements elements = document.getElementsByClass("job-primary");

        List<JobInfo> jobInfos = new ArrayList<>();
        for (Element element : elements) {
            JobInfo jobInfo = new JobInfo();

            //职位
            String name = element.getElementsByClass("job-name").select("a").first().text();
            jobInfo.setPosition(name);

            //位置
            String site = element.getElementsByClass("job-area").text();
            jobInfo.setSite(site);

            String salary = element.getElementsByClass("red").text();
            String[] split = salary.split("-");
            String[] ks = split[1].split("K");
            split[1] = ks[0];

            jobInfo.setLowSalary(Integer.parseInt(split[0]));
            jobInfo.setHighSalary(Integer.parseInt(split[1]));

            String experience = element.select("p").first().text();
            if (experience.contains("年")) {
                String[] experienceAndEducation = experience.split("年");
                jobInfo.setExperience(experienceAndEducation[0]);
                jobInfo.setEducation(experienceAndEducation[1]);
            }

            String company = element.getElementsByClass("name").select("a").first().text();
            jobInfo.setCompany(company);

            jobInfos.add(jobInfo);
        }

        return jobInfos;
    }

    public static void writeExcel(List<JobInfo> infos) throws IllegalAccessException {
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("职位信息");

        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);

        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("职位");

        cell = row.createCell(1);
        cell.setCellValue("地点");

        cell = row.createCell(2);
        cell.setCellValue("最低薪资");

        cell = row.createCell(3);
        cell.setCellValue("最高薪资");

        cell = row.createCell(4);
        cell.setCellValue("工作经验");

        cell = row.createCell(5);
        cell.setCellValue("学历要求");

        cell = row.createCell(6);
        cell.setCellValue("公司");

        //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值


        for (int i = 1; i <= infos.size(); i++) {
            JobInfo info = infos.get(i-1);

            row = sheet.createRow(i);

            Field[] declaredFields = info.getClass().getDeclaredFields();
            for (int j = 0; j < declaredFields.length; j++) {
                cell = row.createCell(j);
                declaredFields[j].setAccessible(true);
                cell.setCellValue(String.valueOf(declaredFields[j].get(info)));
            }
        }
        File file = new File("D:/job.xls");
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
