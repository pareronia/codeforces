package com.github.pareronia.codeforces._1375.b;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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
import java.util.function.Supplier;

/**
 * B. Neighbor Grid
 * @see <a href="https://codeforces.com/problemset/problem/1375/B">https://codeforces.com/problemset/problem/1375/B</a>
 */
public class Main {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.sample = sample;
        this.in = in;
        this.out = out;
    }
    
    @SuppressWarnings("unused")
    private void log(final Supplier<Object> supplier) {
        if (!sample) {
            return;
        }
        System.out.println(supplier.get());
    }
    
    private static final int[][] DIRECTIONS = new int[][] {
        new int[] { -1, 0 },
        new int[] { 1, 0 },
        new int[] { 0, -1 },
        new int[] { 0, 1 }
    };
    
    private int neighbours(final int[][] g, final int n, final int m, final int r, final int c) {
        int ans = 0;
        for (int j = 0; j < DIRECTIONS.length; j++) {
            final int rr = r + DIRECTIONS[j][0];
            final int cc = c + DIRECTIONS[j][1];
            if (0 <= rr && rr < n && 0 <= cc && cc < m) {
                ans++;
            }
        }
        return ans;
    }
    
    private void printGrid(final int[][] grid, final int n, final int m) {
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                this.out.print(grid[r][c]);
                this.out.print(" ");
            }
            this.out.println();
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int m = sc.nextInt();
        final int[][] g = new int[n][m];
        for (int j = 0; j < n; j++) {
            g[j] = sc.nextIntArray(m);
        }
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
               final int nbs = neighbours(g, n, m, r, c);
               if (g[r][c] > nbs) {
                   this.out.println("NO");
                   return;
               }
               g[r][c] = nbs;
            }
        }
        this.out.println("YES");
        printGrid(g, n, m);
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
            final List<String> expected = Files.readAllLines(path).stream()
                    .filter(l -> "NO".equals(l) || "YES".equals(l))
                    .collect(toList());
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual.stream()
                    .filter(l -> "NO".equals(l) || "YES".equals(l))
                    .collect(toList()))) {
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
        
        @SuppressWarnings("unused")
        public long nextLong() {
            return Long.parseLong(next());
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
