package com.mall.austen.entity;

import java.io.Serializable;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/9 18:00
 */
public class ResultVO implements Serializable {

    private static final long serialVersionUID = -3687414601228491545L;

    private Integer code;

    private String msg;

    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
