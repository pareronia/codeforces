package com.github.pareronia.codeforces._1549.c;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.github.pareronia.codeforces.MainTestBase;

@Timeout(2)
public class Round1549CTestCase extends MainTestBase<Main> {

    public Round1549CTestCase() {
        super(Main.class);
    }
    
    @Test
    void test4() throws Throwable {
        final Random rand = new Random(System.nanoTime());
        final List<String> result = runWithTempFile(writer -> {
            writer.write("200000 0");
            writer.newLine();
            writer.write("200000");
            writer.newLine();
            for (int i = 0; i < 199_999; i++) {
                final int n1 = 1 + rand.nextInt(200_000);
                int n2 = n1;
                while (n1 == n2) {
                    n2 = 1 + rand.nextInt(200_000);
                }
                writer.write(String.format("1 %d %d", n1, n2));
                writer.newLine();
            }
            writer.write("3");
            writer.newLine();
        });
        
        System.out.println(result);
    }
}
