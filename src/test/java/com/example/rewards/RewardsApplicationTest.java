package com.example.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RewardsApplication.class)
class RewardsApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodLoadsApplication() {
        RewardsApplication.main(new String[] {"--spring.main.web-application-type=none"});
    }
}
