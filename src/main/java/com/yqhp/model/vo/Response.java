package com.yqhp.model.vo;

import lombok.Data;

/**
 * Created by jiangyitao.
 * 响应对象
 */
@Data
public class Response {

    private static final Integer SUCCESS = 1;
    private static final Integer FAIL = 0;
    private static final Integer ERROR = -1;

    private Integer status;
    private String msg;
    private Object data;

    public static Response success(Object data) {
        Response response = new Response();
        response.setMsg("success");
        response.setStatus(SUCCESS);
        response.setData(data);
        return response;
    }

    public static Response success(String msg) {
        Response response = new Response();
        response.setMsg(msg);
        response.setStatus(SUCCESS);
        response.setData(null);
        return response;
    }

    public static Response success(String msg, Object data) {
        Response response = new Response();
        response.setMsg(msg);
        response.setStatus(SUCCESS);
        response.setData(data);
        return response;
    }

    public static Response fail(String msg) {
        Response response = new Response();
        response.setMsg(msg);
        response.setStatus(FAIL);
        response.setData(null);
        return response;
    }

    public static Response error(String msg) {
        Response response = new Response();
        response.setMsg(msg);
        response.setStatus(ERROR);
        response.setData(null);
        return response;
    }

}
