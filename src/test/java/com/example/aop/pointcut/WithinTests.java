package com.example.aop.pointcut;


import com.example.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * within 지시자는 특정 타입 내의 조인 포인트에 대한 매칭을 제한함.
 * 해당 타입이 매칭되면 그안의 매서드(조인 포인트)들이 자동으로 매칭.
 * 문법은 단순한데 'execution'에서 타입 부분만 사용한다고 보면 됨.
 * */
public class WithinTests {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    /* 테스트 코드가 실행되기 전 helloMethod변수에 'hello' method의 정보 값을 주입.*/
    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // method name , param type
    }

    /**
     * execution의 패키지 타입에 대한 지시자
     * But 표현식에 부모타입을 지정하면 안되고, 정확하게 타입이 맞아야 함.
     * */

    /* 정확한 타입 허용 */
    @Test
    void withinExactPackage() {
        pointcut.setExpression("within(com.example.aop.member.MemberServiceImpl)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 패키지 경론 내 포함된 명인 경우 허용 */
    @Test
    void withinContainPackage() {
        pointcut.setExpression("within(com.example.aop.member.*Service*)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 직전 하위 패키지 경우 허용 */
    @Test
    void withinSubPackage1() {
        pointcut.setExpression("within(com.example.aop.member.*)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 하위 패키지 경우 모두 허용 */
    @Test
    void withinSubPackage2() {
        pointcut.setExpression("within(com.example.aop..*)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* execution은 타입 기반, 인터페이스 선정이 가능하지맘 within은 인터페이스 선정 불가*/
    @Test
    @DisplayName("타겟의 타입에만 직접 적용, 인터페이스를 선정하면 안된다.")
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(com.example.aop.member.MemberService)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }



}
