package com.mall.austen.entity;

import java.io.Serializable;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/9 17:59
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -9055917019209611116L;

    private Integer id;

    private String name;

    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
