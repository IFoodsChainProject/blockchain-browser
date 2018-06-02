package com.ifoods.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifoods.common.model.CodeMsg;
import com.ifoods.common.model.ResponseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月28日
 */
@Aspect
@Component
@Slf4j
public class ExceptionAspect {

    @Pointcut("execution(public * com.ifoods.controller.*.*(..))")
    public void controllerException(){
        
    }

    @Before("controllerException()")
    public void doBefore(JoinPoint joinPoint){
        
    }

    /**
     * controller层异常处理切面
     */
    @Around("controllerException()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            log.error("system error", e);
            return new ResponseModel(CodeMsg.SYSTEM_ERROR);
        }
    }
    
    @AfterReturning(pointcut = "controllerException()",returning = "object")
    public void doAfterReturing(Object object){
        //log.info("return obj={}", object);
    }
}
