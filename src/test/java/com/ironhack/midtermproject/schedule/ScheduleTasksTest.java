package com.ironhack.midtermproject.schedule;

import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleTasksTest {


    @SpyBean
    private ScheduleTasks scheduleTasks;

    @BeforeEach
    void setUp() {
    }

    @Test
    void savingInteresteRate() {
    }

    @Test
    void creditCardInterestRate() {
    }

    @Test
    void fraudDetection24Hours() {
    }
}