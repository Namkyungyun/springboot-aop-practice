package com.example.aop.order.aop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {
     /* pointcut 분리하기 */
    @Pointcut("execution(* com.example.aop.order..*(..))")
    public void allOrder() {} // pointcut signature

    @Around("allOrder()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature().toShortString()); //join point 시그니처
        return joinPoint.proceed();
    }
}
