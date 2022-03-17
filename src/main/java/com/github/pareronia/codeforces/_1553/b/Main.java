package com.github.pareronia.codeforces._1553.b;

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
import java.util.stream.Stream;

/**
 * B. Reverse String
 * @see <a href="https://codeforces.com/contest/1553/problem/B">https://codeforces.com/contest/1553/problem/B</a>
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
    
    private int countChars(final String s) {
        return (int) s.chars().distinct().count();
    }
    
    private Result<?> handleTestCase2(final Integer i, final FastScanner sc) {
        String ans = "NO";
        final String s = sc.next();
        final String t = sc.next();
        final int slen = s.length();
        final int tlen = t.length();
        outer:
        for (int j = 0; j < slen; j++) {
            for (int k = j; k < slen; k++) {
                for (int m = 0; m < tlen - (k - j); m++) {
                    if (k - m < 0) {
                        continue;
                    }
                    final String c = s.substring(j, k)
                            + s.substring(k - m, k);
                    if (c.equals(t)) {
                        ans = "YES";
                        break outer;
                    }
                }
            }
        }
        return new Result<>(i, List.of(ans));
    }
    
    private Result<?> handleTestCase(final Integer i, final FastScanner sc) {
        final String s = sc.next();
        final String t = sc.next();
        
        String ans;
        if (t.length() > s.length()) {
            if (countChars(s) == 1 &&  countChars(t) == 1) {
                ans = "YES";
            } else {
                ans = "NO";
            }
        } else if (s.contains(t)) {
            ans = "YES";
        } else {
            ans = "NO";
            for (int j = t.length(); j > 0; j--) {
                if (s.contains(t.substring(0, j))) {
                    final char[] tt = new char[t.length() - j];
                    for (int k = t.length() - 1; k >= j; k--) {
                        tt[Math.abs(s.length() - 1 - k)] = t.charAt(k);
                    }
                    if (s.contains(new String(tt))) {
                        ans = "YES";
                        break;
                    }
                }
            }
        }
        
        return new Result<>(i, List.of(ans));
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = sc.nextInt();
            final List<Result<?>> results
                    = Stream.iterate(1, i -> i <= numberOfTestCases, i -> i + 1)
                            .map(i -> handleTestCase2(i, sc))
                            .collect(toList());
            output(results);
        }
    }

    private void output(final List<Result<?>> results) {
        results.forEach(r -> {
            r.getValues().stream().map(Object::toString).forEach(this.out::println);
        });
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
    
    private static final class Result<T> {
        @SuppressWarnings("unused")
        private final int number;
        private final List<T> values;
        
        public Result(final int number, final List<T> values) {
            this.number = number;
            this.values = values;
        }

        public List<T> getValues() {
            return values;
        }
    }
}
