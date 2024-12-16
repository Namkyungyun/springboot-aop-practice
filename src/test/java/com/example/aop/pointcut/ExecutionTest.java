package com.example.aop.pointcut;

import com.example.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    /* 테스트 코드가 실행되기 전 helloMethod변수에 'hello' method의 정보 값을 주입.*/
    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // method name , param type
    }

    @Test
    void printMethod() {
        log.info("helloMethod={}", helloMethod);
    }

    /* 모든 조건절에 strict하게 확인 */
    @Test
    void exactMatch() {
        // pointcut에 적용할 조건 넣기
        pointcut.setExpression("execution(public String com.example.aop.member.MemberServiceImpl.hello(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 모든 반환타입 허용, 모든 메서드 허용, 모든 파라미터 허용('..'은 파라미터 타입과 수가 상관없다는 뜻) */
    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 모든 조건 중에서 설정한 명과 동일한 method명을 가지면 ok*/
    @Test
    void exactNameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 모든 조건 중에서 메서드명의 시작이 hel로 시작하면 ok */
    @Test
    void prefixNameMatch() {
        pointcut.setExpression("execution(* hel*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 모든 조건 중에서 메서드명에 el이 포함되면 ok */
    @Test
    void containNameMatch() {
        pointcut.setExpression("execution(* *el*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 실패 테스트 */
    @Test
    void failNameMatch() {
        pointcut.setExpression("execution(* nono(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /* 정확한 패키지명과 메서드명을 가진 경우 ok */
    @Test
    void exactPackageMatch() {
        pointcut.setExpression("execution(* com.example.aop.member.MemberServiceImpl.hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* member 하위 패키지 내 모든 메소드 시 ok*/
    @Test
    void containsPackageMatch1() {
        pointcut.setExpression("execution(* com.example.aop.member.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* aop 하위 패키지 내 모든 메소드 시 ok*/
    @Test
    void containsPackageMatch2() {
        pointcut.setExpression("execution(* com.example.aop..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 패키지 매칭 실패 케이스 */
    @Test
    void failPackageMatch() {
        pointcut.setExpression("execution(* com.example.aop.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 패키지 타입 매칭 테스트 :: 부모 타입 허용
     * */
    @Test
    void exactTypeMatch() {
        pointcut.setExpression("execution(* com.example.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* Execution에서는 부모타입을 선언해도 그 자식타입은 매칭됨. 다형성이 할당가능하다는 점과 상통 */
    @Test
    void superTypeMatch() {
        pointcut.setExpression("execution(* com.example.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /* 부모타입에서 정의되어지지 않고 MemeberServiceImpl에서만 정의된 경우 fail */
    @Test
    void failInternalMethodTypeMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.example.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    /* MemeberServiceImpl에서만 정의된 경우의 success */
    @Test
    void successInternalMethodTypeMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.example.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }


    /**
     * 파라미터 매칭
     * */
    /* String 타입의 파라미터 허용 */
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isTrue();
    }

    /* 파라미터가 없는 경우만 허용 */
    @Test
    void emptyArgsMatch() {
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isFalse();
    }

    /* 정확히 하나의 파라미터만 허용, 모든 입은 허용 */
    @Test
    void onlyOneArgsMatch() {
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isTrue();
    }

    /* 숫자와 무관하게 모든 파라미터, 모든 타입 허용 */
    @Test
    void allArgsMatch() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isTrue();
    }

    /* 찻반제 인자가 String타입으로 시작, 그 뒤의 인자들은 숫자와 무관, 타입 무관 허용  */
    @Test void firstArgsExactMatch() {
        pointcut.setExpression("execution(* *(String, ..))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isTrue();
    }

    /* 두개의 파라미터 허용, 두 개의 파라미터 모두 String  */
    @Test
    void twoArgsExactMatch() {
        pointcut.setExpression("execution(* *(String, String))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isFalse();
    }

    /* 첫 번째 파라미터는 String, 두번째 파라미터는 모두 허용  */
    @Test
    void twoArgsComplexMatch() {
        pointcut.setExpression("execution(* *(String, *))");
        Assertions.assertThat(pointcut.matches(helloMethod, String.class)).isFalse();
    }

}
