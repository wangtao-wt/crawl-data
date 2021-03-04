package com.wt.crawl.pojo;

import lombok.Data;

/**
 * @author wangtao
 */

@Data
public class JobInfo {
    /**
     * 职位
     */
    private String position;

    /**
     * 地点
     */
    private String site;

    /**
     * 薪资 最低 单位k
     */
    private int lowSalary;

    /**
     * 薪资 最高 单位k
     */
    private int HighSalary;

    /**
     * 工作经验
     */
    private String experience;

    /**
     * 学历要求
     */
    private String education;

    /**
     * 公司
     */
    private String company;
}
