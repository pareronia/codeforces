package com.github.pareronia.codeforces.gym._102697._054;

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
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 054. When is my meeting?
 * @see <a href="https://codeforces.com/gym/102697/problem/054">https://codeforces.com/gym/102697/problem/054</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("January",   0),
            Map.entry("February",  1),
            Map.entry("March",     2),
            Map.entry("April",     3),
            Map.entry("May",       4),
            Map.entry("June",      5),
            Map.entry("July",      6),
            Map.entry("August",    7),
            Map.entry("September", 8),
            Map.entry("October",   9),
            Map.entry("November",  10),
            Map.entry("December",  11)
    );
    private static int[] DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    
    private int dayNum(final String s) {
        final String[] ss = s.split(" ");
        final int month = MONTHS.get(ss[0]);
        final int day = Integer.parseInt(ss[1].replace(",", ""));
        final int year = Integer.parseInt(ss[2]);
        return year * 365
                + IntStream.range(0, month).map(j -> DAYS[j]).sum()
                + day;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int d1 = dayNum(sc.next());
        final int d2 = dayNum(sc.next());
        final int diff = d1 - d2;
        String ans;
        if (diff > 0) {
            ans = String.format("Sorry you are %d days late", diff);
        } else if (diff < 0) {
            ans = String.format("The meeting will occur in %d days.", -diff);
        } else {
            ans = "It is today";
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
        
        public FastScanner(final InputStream in) {
            this.br = new BufferedReader(new InputStreamReader(in));
        }
        
        public String next() {
            try {
                return br.readLine();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
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
