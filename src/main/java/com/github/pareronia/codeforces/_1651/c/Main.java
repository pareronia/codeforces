package com.github.pareronia.codeforces._1651.c;

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
import java.util.stream.LongStream;

/**
 * C. Fault-tolerant Network
 * @see <a href="https://codeforces.com/contest/1651/problem/C">https://codeforces.com/contest/1651/problem/C</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private long cost(final int[] a, final int[] b, final int... idxs) {
        long ans = 0;
        for (int i = 0; i < idxs.length; i += 2) {
            ans += Math.abs(a[idxs[i]] - b[idxs[i + 1]]);
        }
        return ans;
    }
    
    private int best(final int[] ar, final int from) {
        final long best = Long.MAX_VALUE;
        int pos = -1;
        for (int j = 0; j < ar.length; j++) {
            final int cost = Math.abs(ar[j] - from);
            if (cost < best) {
                pos = j;
            }
        }
        return pos;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[] a = sc.nextIntArray(n);
        final int[] b = sc.nextIntArray(n);
        final long ans = LongStream.of(
                    cost(a, b, 0, 0, n - 1, 0, best(a, b[n - 1]), n - 1),
                    cost(a, b, 0, 0, n - 1, best(b, a[n - 1]), best(a, b[n - 1]), n - 1),
                    cost(a, b, 0, 0, n - 1, n - 1),
                    cost(a, b, 0, best(b, a[0]), n - 1, 0, best(a, b[n - 1]), n - 1),
                    cost(a, b, 0, best(b, a[0]), n - 1, best(b, a[n - 1]), best(a, b[n - 1]), n - 1, best(a, b[0]), 0),
                    cost(a, b, 0, best(b, a[0]), n - 1, n - 1, best(a, b[0]), 0),
                    cost(a, b, 0, n - 1, n - 1, 0),
                    cost(a, b, 0, n - 1, n - 1, best(b, a[n - 1]), best(a, b[0]), 0),
                    cost(a, b, 0, n - 1, n - 1, n - 1, best(a, b[0]), 0)
                )
                .min().orElseThrow();
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = sc.nextInt();
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
        
        public int[] nextIntArray(final int n) {
            final int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
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
