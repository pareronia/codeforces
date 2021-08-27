package com.github.pareronia.codeforces._1562.b;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * B. Scenes From a Memory
 * @see <a href="https://codeforces.com/contest/1562/problem/B">https://codeforces.com/contest/1562/problem/B</a>
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

    private void powerSet(final String s, final Function<String, Boolean> cont) {
        final BigInteger size = BigInteger.TWO.pow(s.length());
        BigInteger i = BigInteger.ONE;
        while (i.compareTo(size) < 0) {
            if (!check(s, cont, i)) {
                return;
            }
            i = i.multiply(BigInteger.TWO);
        }
        i = BigInteger.ZERO;
        while (i.compareTo(size) < 0) {
            if (!check(s, cont, i)) {
                return;
            }
            i = i.add(BigInteger.ONE);
        }
    }

    private boolean check(final String s, final Function<String, Boolean> cont, final BigInteger i) {
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < s.length(); j++) {
            if (i.and(BigInteger.ONE.shiftLeft(j)).compareTo(BigInteger.ZERO) > 0) {
                sb.append(s.charAt(j));
            }
        }
        final String ans = sb.toString();
        if (ans != null && !ans.isEmpty()) {
            return cont.apply(ans);
        }
        return true;
    }
    
    private boolean isPrime(final Long number) {
        if (number == 1) {
            return false;
        }
        final long start = (int) Math.floor(Math.sqrt(number));
        for (long i = start; i >= 2; i--) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        sc.nextInt();
        final String n = sc.next();
        powerSet(n, t -> {
            final Long ans = Long.valueOf(t);
            if (!isPrime(ans)) {
                this.out.println(t.length());
                this.out.println(ans);
                return false;
            } else {
                return true;
            }
        });
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
        
        @SuppressWarnings("unused")
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
