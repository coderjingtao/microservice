package com.joseph.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.function.Function;

@SpringBootTest
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        Function<Integer, Integer> addThree = value -> value + 3;
        Integer result = addThree.apply(8);
        System.out.println(result);
    }

    @Test
    void testTimeFormat(){
        DateTimeFormatter shortFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println(shortFormatter.format(LocalTime.now()));
    }
}
