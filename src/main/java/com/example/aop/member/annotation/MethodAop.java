package com.example.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메서드에 붙이는 애노테이션이라는 설정
@Retention(RetentionPolicy.RUNTIME) // 동적으로 읽을 수 있음.
public @interface MethodAop {
    String value() default "";
}
