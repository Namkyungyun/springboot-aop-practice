package com.example.aop.exam;

import com.example.aop.exam.aspect.RetryAspect;
import com.example.aop.exam.aspect.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Slf4j
@Import({TraceAspect.class, RetryAspect.class})
public class ExamTest {

    @Autowired
    private ExamService examService;

    @Test
    void test() {
        for(int i =0; i< 5; i++) {
            log.info("[test] test = {}", i);
            examService.request("data" + i);
        }

    }
}
