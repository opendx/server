package com.fgnb.controller;

import com.fgnb.exception.BusinessException;
import com.fgnb.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiangyitao.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 没处理到的异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error("未处理的异常", e);
        return Response.error(e.getMessage());
    }

    /**
     * BusinessException
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Response handleBusinessException(BusinessException e){
        return Response.fail(e.getMessage());
    }

    /**
     * 参数校验不通过，非@RequestBody
     *
     * @param e
     * @param req
     * @return
     */
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public Response handleBindException(BindException e, HttpServletRequest req) {
        log.info("[{}]参数校验失败:{}", req.getServletPath(), e.getFieldError().getDefaultMessage());
        return Response.fail(e.getFieldError().getDefaultMessage());
    }

    /**
     * 参数校验不通过，@RequestBody
     *
     * @param e
     * @param req
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        log.info("[{}]参数校验失败:{}", req.getServletPath(), e.getBindingResult().getFieldError().getDefaultMessage());
        return Response.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

}
