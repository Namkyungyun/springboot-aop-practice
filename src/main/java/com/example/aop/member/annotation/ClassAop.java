package com.example.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스에 붙이는 애노테이션이라는 설정
@Retention(RetentionPolicy.RUNTIME) // 런타임시 까지 살아 있는 애노테이션
public @interface ClassAop {
}
