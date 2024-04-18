/*
  Copyright (C), 2009-2019, 江苏汇博机器人技术股份有限公司
  FileName: ResponseResult
  Author:   ShuangPC
  Date:     2019/5/31
  Description:
  History:
  <author>         <time>          <version>          <desc>
  作者姓名         修改时间           版本号             描述
 */

package com.zhaoyunfei.mqtt.mqtt.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 响应结果
 *
 * @author zhaoyunfei
 */

@Data
@ToString
public class ResponseResult<T> {
    /**
     * 默认成功响应的消息
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "响应成功";

    /**
     * 默认失败响应的消息
     */
    private static final String DEFAULT_FAILURE_MESSAGE = "响应失败";

    /**
     * 时间格式
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据内容
     */
    private T data;

    /**
     * 错误消息或正确消息
     */
    private String message;

    /**
     * 响应创建时间
     */
    private String time;

    /**
     * 而jackson的反序列化需要无参构造函数(必须写)
     */
    public ResponseResult() {
    }


    /**
     * 构造方法
     *
     * @param success 是否成功
     * @param message 错误消息或正确消息
     * @param data    数据内容
     */
    private ResponseResult(Boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }

    /**
     * 构造方法
     *
     * @param success 是否成功
     * @param message 错误消息或正确消息
     */
    private ResponseResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    /**
     * 构造方法
     *
     * @param success 是否成功
     */
    private ResponseResult(Boolean success) {
        this(success, "");
    }

    /**
     * 默认成功响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> succeed() {
        return new ResponseResult<>(true, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 携带数据的成功响应
     *
     * @param data 数据
     * @param <T>  数据泛型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> succeed(T data) {
        return succeed(DEFAULT_SUCCESS_MESSAGE, data);
    }

    /**
     * 携带数据和消息的成功响应
     *
     * @param data    数据
     * @param message 错误消息或正确消息
     * @param <T>     数据泛型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> succeed(String message, T data) {
        return new ResponseResult<>(true, message, data);
    }

    /**
     * 默认失败响应
     *
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail() {
        return fail(DEFAULT_FAILURE_MESSAGE);
    }


    /**
     * 携带消息的失败响应
     *
     * @param message 错误消息或正确消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>(false, message);
    }
}
