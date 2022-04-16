package com.github.pareronia.codeforces._1234.d;

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
import java.util.TreeSet;

/**
 * D. Distinct Characters Queries
 * @see <a href="https://codeforces.com/contest/1234/problem/D">https://codeforces.com/contest/1234/problem/D</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    @SuppressWarnings("unchecked")
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final String s = sc.next();
        final int q = sc.nextInt();
        final TreeSet<Integer>[] map = new TreeSet[26];
        for (int j = 0; j < 26; j++) {
            map[j] = new TreeSet<>();
        }
        for (int j = 0; j < s.length(); j++) {
            map[s.charAt(j) - 'a'].add(j);
        }
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < q; j++) {
            if (sc.nextInt() == 1) {
                final int pos = sc.nextInt() - 1;
                final int c = sc.next().charAt(0) - 'a';
                for (int k = 0; k < 26; k++) {
                    map[k].remove(pos);
                }
                map[c].add(pos);
            } else {
                final int l = sc.nextInt() - 1;
                final int r = sc.nextInt() - 1;
                int cnt = 0;
                for (int k = 0; k < 26; k++) {
                    final Integer lb = map[k].ceiling(l);
                    if (lb != null && lb <= r) {
                        cnt++;
                    }
                }
                sb.append(cnt).append(System.lineSeparator());
            }
        }
        final String ans = sb.toString();
        this.out.print(ans);
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
