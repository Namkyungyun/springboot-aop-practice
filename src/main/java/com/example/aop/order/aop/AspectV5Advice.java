package com.example.aop.order.aop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV5Advice {

    @Around("com.example.aop.order.aop.Pointcuts.orderAndService()")
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

    /* (1) 실제 로직이 호출이 진행될 때 */
    @Before("com.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature().toShortString());
    }

    /* (2) 실제 로직이 예외 없이 성공적으로 return으로 연결될 때 */
    @AfterReturning(value = "com.example.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {}, result : {}", joinPoint.getSignature().toShortString(), result);
    }

    /* (3) 실제 로직호출에서 예외가 있을 때 */
    @AfterThrowing(value = "com.example.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[throwing] {}, message : {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    /* (4) returning과 throwing 이후 */
    @After(value = "com.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature().toShortString());
    }
}
