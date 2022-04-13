package com.github.pareronia.codeforces.gym._103647.d;

import org.junit.jupiter.api.Test;

class Gym104647DTest {

    @Test
    void test() {
        for (int i = 2; i <= 100; i++) {
            new Main(true, System.in, System.out, i).solve();
        }
    }
}
