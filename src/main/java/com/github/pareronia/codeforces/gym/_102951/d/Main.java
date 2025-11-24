package com.github.pareronia.codeforces.gym._102951.d;

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
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * D. Static Range Queries
 * @see <a href="https://codeforces.com/gym/102951/problem/D">https://codeforces.com/gym/102951/problem/D</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;

    public Main(final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int q = sc.nextInt();
        final int[] a = new int[(n + q) * 2];
        final int[][] us = new int[n][3];
        final int[][] qs = new int[n][2];
        for (int j = 0; j < n; j++) {
            final int l = sc.nextInt();
            final int r = sc.nextInt();
            final int v = sc.nextInt();
            a[2 * j] = l;
            a[2 * j + 1] = r;
            us[j] = new int[] {l, r, v};
        }
        for (int j = 0; j < q; j++) {
            final int l = sc.nextInt();
            final int r = sc.nextInt();
            a[2 * n + 2 * j] = l;
            a[2 * n + 2 * j + 1] = r;
            qs[j] = new int[] {l, r};
        }
        Arrays.sort(a);
        final long[] d = new long[a.length + 1];
        for (int j = 0; j < n; j++) {
            final int l = Arrays.binarySearch(a, us[j][0]);
            final int r = Arrays.binarySearch(a, us[j][1]);
            final int v = us[j][2];
            d[l + 1] += v;
            d[r + 1] -= v;
        }
        final long[] w = new long[a.length + 1];
        for (int j = 0; j < a.length - 1; j++) {
            w[j + 1] = a[j + 1] - a[j];
        }
        final long[] iv = new long[a.length + 1];
        for (int j = 1; j < a.length; j++) {
            iv[j] = iv[j - 1] + d[j];
        }
        final long[] p = new long[a.length + 1];
        for (int j = 1; j < a.length; j++) {
            p[j] = p[j - 1] + w[j] * iv[j];
        }
        for (int j = 0; j < q; j++) {
            final int l = Arrays.binarySearch(a, qs[j][0]);
            final int r = Arrays.binarySearch(a, qs[j][1]);
            this.out.println(p[r] - p[l]);
        }
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
            final Path path = Paths.get(Main.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format("Expected %s, got %s", expected, actual));
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
