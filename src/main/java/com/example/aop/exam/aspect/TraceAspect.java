package com.example.aop.exam.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TraceAspect {

    /* RetryAspect에서 joinpoint.proceed가 실행되면서 해당 메소드가 불림
    *  그 다음 RetryAspect진행
    * */
    @Before("@annotation(com.example.aop.exam.annotation.Trace)")
    public void doTrace(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[trace] {} args = {}", joinPoint.getSignature(), args);
    }
}
