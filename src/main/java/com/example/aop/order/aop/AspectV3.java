package com.example.aop.order.aop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {
     /* pointcut 분리하기 :: 패키지 하위 모두 허용 */
    @Pointcut("execution(* com.example.aop.order..*(..))")
    public void allOrder() {} // pointcut signature

    /* 클래스 이름 패턴이 *Service */
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature().toShortString()); //join point 시그니처
        return joinPoint.proceed();
    }

    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[transaction start] {}", joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();
            log.info("[transaction commit] {}", joinPoint.getSignature().toShortString());

            return result;

        } catch (Exception e) {
            log.error("[transaction rollback] {}", joinPoint.getSignature().toShortString());
            throw e;
        } finally {
            log.info("[resource release] {}", joinPoint.getSignature().toShortString());
        }
    }
}
