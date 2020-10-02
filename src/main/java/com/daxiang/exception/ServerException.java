package com.daxiang.exception;

/**
 * Created by jiangyitao.
 */
public class ServerException extends RuntimeException {
    public ServerException(String msg) {
        super(msg);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }
}
