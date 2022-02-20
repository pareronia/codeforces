package com.github.pareronia.codeforces._1635.b;

import org.junit.jupiter.api.Test;

import com.github.pareronia.codeforces.MainTestBase;

class Round1635BTestCase extends MainTestBase<Main> {

    protected Round1635BTestCase() {
        super(Main.class);
    }
    
    @Test
    void test() throws Throwable {
        runWithTempFile(writer -> {
            writer.write("1");
            writer.newLine();
            writer.write("100000");
            writer.newLine();
            for (int i = 0; i < 99_999; i++) {
                writer.write(String.valueOf(i));
                writer.write(' ');
            }
            writer.write("100000");
            writer.newLine();
        });
    }
}
