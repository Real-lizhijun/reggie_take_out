package com.reggie;

import com.reggie.pojo.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Slf4j
class ReggieTakeOutApplicationTests {

    @Test
    void contextLoads() {
        String s = UUID.randomUUID().toString();
        log.info(s);
    }

}
