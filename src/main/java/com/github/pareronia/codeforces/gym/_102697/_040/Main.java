package com.github.pareronia.codeforces.gym._102697._040;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

/**
 * 040. Valid Sudoku
 * @see <a href="https://codeforces.com/gym/102697/problem/040">https://codeforces.com/gym/102697/problem/040</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private boolean check(final char[] a) {
        final String s = IntStream.range(0, 9)
            .map(j -> a[j])
            .sorted()
            .collect(
                    StringBuilder::new,
                    (sb, c) -> sb.append((char) c),
                    StringBuilder::append)
            .toString();
        return "123456789".equals(s);
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final char[][] g = new char[9][9];
        for (int j = 0; j < 9; j++) {
            for (int k = 0; k < 9; k++) {
                g[j][k] = sc.next().charAt(0);
            }
        }
        String ans = "VALID";
        for (int r = 0; r < 9; r++) {
            if (!check(g[r])) {
                ans = "INVALID";
                break;
            }
        }
        for (int c = 0; c < 9; c++) {
            final char[] col = new char[9];
            for (int r = 0; r < 9; r++) {
                col[r] = g[r][c];
            }
            if (!check(col)) {
                ans = "INVALID";
                break;
            }
        }
        outer:
        for (int r = 0; r < 9; r += 3) {
            for (int c = 0; c < 9; c += 3) {
                final char[] a = new char[9];
                int m = 0;
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        a[m] = g[r + j][c + k];
                        m++;
                    }
                }
                if (!check(a)) {
                    ans = "INVALID";
                    break outer;
                }
            }
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = isSample() ? sc.nextInt() : 1;
            for (int i = 0; i < numberOfTestCases; i++) {
                handleTestCase(i, sc);
            }
        }
    }

    public static void main(final String[] args) throws IOException, URISyntaxException {
        final boolean sample = isSample();
        final InputStream is;
        final PrintStream out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long timerStart = 0;
        if (sample) {
            is = Main.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new Main(sample, is, out).solve();
        
        out.flush();
        if (sample) {
            final long timeSpent = (System.nanoTime() - timerStart) / 1_000;
            final double time;
            final String unit;
            if (timeSpent < 1_000) {
                time = timeSpent;
                unit = "µs";
            } else if (timeSpent < 1_000_000) {
                time = timeSpent / 1_000.0;
                unit = "ms";
            } else {
                time = timeSpent / 1_000_000.0;
                unit = "s";
            }
            final Path path
                    = Paths.get(Main.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format(
                        "Expected %s, got %s", expected, actual));
            }
            actual.forEach(System.out::println);
            System.out.println(String.format("took: %.3f %s", time, unit));
        }
    }
    
    private static boolean isSample() {
        try {
            return "sample".equals(System.getProperty("codeforces"));
        } catch (final SecurityException e) {
            return false;
        }
    }
    
    private static final class FastScanner implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;
        
        public FastScanner(final InputStream in) {
            this.br = new BufferedReader(new InputStreamReader(in));
            st = new StringTokenizer("");
        }
        
        public String next() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
    
        public int nextInt() {
            return Integer.parseInt(next());
        }
        
        @Override
        public void close() {
            try {
                this.br.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }
}
