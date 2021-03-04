package com.wt.crawl.demo;

import com.wt.crawl.http.HttpDownloadDynamic;
import com.wt.crawl.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetBossInfos {

    public static void main(String[] args) throws NoSuchMethodException, ScriptException, IOException {
        //https://www.zhipin.com/c101220100/?page=1&ka=page-1

        String s = "";
        for (int i = 1; i < 4; i++) {
            String url = "https://www.zhipin.com/c101220100/?page=" + i + "&ka=page-" + i;
            s += HttpDownloadDynamic.sendGet(url);
        }
        List<JobInfo> jobInfos = parseHtml(s);
        for (JobInfo jobInfo : jobInfos) {
            System.out.println(jobInfo);
        }
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
}
