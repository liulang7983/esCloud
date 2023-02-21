package com.bean;


import lombok.Data;
import lombok.ToString;

/**
 * @author ming.li
 * @date 2022/11/15 10:36
 */
@Data
@ToString
public class EsDb {
    private String id;
    private String name;
    private Integer sex;
    private Integer age;
    private String address;
    private String remark;
}
