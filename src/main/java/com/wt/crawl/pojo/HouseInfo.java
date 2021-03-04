package com.wt.crawl.pojo;

import lombok.Data;

/**
 * @author wangtao
 */
@Data
public class HouseInfo {

    /**
     * 小区名
     */
    private String name;
    /**
     * 户型大小
     */
    private String houseType;
    /**
     * 面积
     */
    private String area;
    /**
     * 方位
     */
    private String position;
    /**
     * 单价
     */
    private String unitPrice;
    /**
     * 总价
     */
    private String totalPrice;
    /**
     * 年代
     */
    private String years;

}
