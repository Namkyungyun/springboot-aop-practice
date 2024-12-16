package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    // com.example.aop.order 패키지의 하위 패키지 pointcut 설정
    @Around("execution(* com.example.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature().toShortString());

        // 로직 실행 코드를 넣어야 실제 로직 코드로 연결됨.
        return joinPoint.proceed();
    }

}
