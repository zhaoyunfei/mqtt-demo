/*
  Copyright (C), 2009-2019, 江苏汇博机器人技术股份有限公司
  FileName: CustomException
  Author:   xingshuang
  Date:     2019/4/26
  Description:
  History:
  <author>          <time>          <version>          <desc>
  作者姓名           修改时间           版本号              描述
 */

package com.zhaoyunfei.mqtt.exceptions;


import org.springframework.http.HttpStatus;

/**
 * 自定义异常类型
 *
 * @author xingshuang
 * @since 2019/4/26
 */
public class CustomException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
