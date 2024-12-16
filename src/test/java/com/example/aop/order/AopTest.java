package com.example.aop.order;

import com.example.aop.order.aop.AspectV1;
import com.example.aop.order.aop.AspectV2;
import com.example.aop.order.aop.AspectV3;
import com.example.aop.order.aop.AspectV5Advice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
@Import(AspectV5Advice.class)
public class AopTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void aspectV1ProxyInfo() {
        log.info("orderService isProxy? {}", AopUtils.isAopProxy(orderService));
        log.info("orderRepository isProxy? {}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void aspectV1SuccessTest() {
        orderService.orderItem("");
    }


    @Test
    void aspectV1FailTest() {
        Assertions.assertThatThrownBy(() -> orderService.orderItem("ex")).isInstanceOf(IllegalStateException.class);
    }


}
