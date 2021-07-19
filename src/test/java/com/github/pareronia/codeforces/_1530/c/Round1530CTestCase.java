package com.github.pareronia.codeforces._1530.c;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.github.pareronia.codeforces.MainTestBase;

@Timeout(2)
class Round1530CTestCase extends MainTestBase<Main> {

    public Round1530CTestCase() {
        super(Main.class);
    }

    @Test
    void test4() throws Throwable {
        final List<String> result = runWithTempFile(writer -> {
            writer.write("1");
            writer.newLine();
            writer.write("100000");
            writer.newLine();
            for (int i = 0; i < 100_000; i++) {
                writer.write("0 ");
            }
            writer.newLine();
            for (int i = 0; i < 100_000; i++) {
                writer.write("100 ");
            }
            writer.newLine();
        });
        
        assertThat(result, is(List.of("100000")));
    }
    
    @Test
    void test5() throws Throwable {
        runWithTempFile(writer -> {
            writer.write("1");
            writer.newLine();
            writer.write("100000");
            writer.newLine();
            final Random random = new Random(System.nanoTime());
            for (int i = 0; i < 100_000; i++) {
                writer.write(String.valueOf(random.nextInt(101)));
                writer.write(" ");
            }
            writer.newLine();
            for (int i = 0; i < 100_000; i++) {
                writer.write(String.valueOf(random.nextInt(101)));
                writer.write(" ");
            }
            writer.newLine();
        });
    }
}
