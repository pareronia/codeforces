package com.github.pareronia.codeforces._0468.b;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * B. Two Sets
 * @see <a href="https://codeforces.com/contest/468/problem/B">https://codeforces.com/contest/468/problem/B</a>
 */
public class Main {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;

    public Main(final Boolean sample, final InputStream in, final PrintStream out) {
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

    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = this.sample ? sc.nextInt() : 1;
            for (int t = 0; t < numberOfTestCases; t++) {
                final int n = sc.nextInt();
                final int a = sc.nextInt();
                final int b = sc.nextInt();
                final int[] p = sc.nextIntArray(n);
                final DSU dsu = new DSU(n + 2);
                final Map<Integer, Integer> map = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    map.put(p[i], i + 2);
                }
                for (int i = 0; i < n; i++) {
                    dsu.union(i + 2, map.getOrDefault(a - p[i], 1));
                    dsu.union(i + 2, map.getOrDefault(b - p[i], 0));
                }
                final int ra = dsu.find(0);
                if (ra != dsu.find(1)) {
                    this.out.println("YES");
                    this.out.println(
                            IntStream.range(0, n)
                                    .mapToObj(i -> dsu.find(map.get(p[i])) == ra ? "0" : "1")
                                    .collect(joining(" ")));
                } else {
                    this.out.println("NO");
                }
            }
        }
    }

    private static final class DSU {
        private final int[] id;
        private final int[] size;

        public DSU(final int size) {
            this.id = new int[size];
            this.size = new int[size];
            for (int i = 0; i < size; i++) {
                id[i] = i;
            }
            Arrays.fill(this.size, 1);
        }

        public boolean union(final int a, final int b) {
            final int rootA = find(a);
            final int rootB = find(b);
            if (rootA == rootB) {
                return false;
            }
            if (size[rootB] > size[rootA]) {
                return union(rootB, rootA);
            }
            id[rootB] = rootA;
            size[rootA] += size[rootB];
            return true;
        }

        public int find(final int n) {
            if (id[n] == n) {
                return n;
            }
            id[n] = find(id[n]);
            return id[n];
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
