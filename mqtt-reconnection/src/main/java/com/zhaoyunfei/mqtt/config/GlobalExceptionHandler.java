package com.zhaoyunfei.mqtt.config;



import com.zhaoyunfei.mqtt.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author ShuangPC
 * @since 2019/2/20
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 自定义异常的返回结果
     *
     * @param e 异常
     * @return 自定义的返回结果
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<String> customException(CustomException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    /**
     * 运行时的异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> customException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * 异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * 方法输入参数校验异常，对应于对象
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String error = "参数输入错误：" + e.getBindingResult().getFieldErrors().stream()
                .map(x -> "[" + x.getField() + "]" + x.getDefaultMessage())
                .collect(Collectors.joining("，"));
        log.error(error, e);
        return ResponseEntity.badRequest().body(error);
    }


    /**
     * 异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseEntity<String> BindExceptionErrorHandler(BindException e) {
        log.error(e.getMessage(), e);
        String error = "参数输入错误：" + e.getBindingResult().getFieldErrors().stream()
                .map(x -> "[" + x.getField() + "]" + x.getDefaultMessage())
                .collect(Collectors.joining("，"));
        log.error(error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
