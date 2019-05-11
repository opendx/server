package com.fgnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private static Response buildResponse(Integer status, String msg, Object data) {
        Response response = new Response();
        response.setStatus(status);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return status == SUCCESS ? true : false;
    }

    public static Response success(Object data) {
        return buildResponse(SUCCESS, "success", data);
    }

    public static Response success(String msg) {
        return buildResponse(SUCCESS, msg, null);
    }

    public static Response success(String msg, Object data) {
        return buildResponse(SUCCESS, msg, data);
    }

    public static Response fail(String msg) {
        return buildResponse(FAIL, msg, null);
    }

    public static Response error(String msg) {
        return buildResponse(ERROR, msg, null);
    }

}
