package com.github.pareronia.codeforces.gym._102697._051;

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

/**
 * 051. The Convergence of Two Tetra-sided Polygons, Known as Rectangles
 * @see <a href="https://codeforces.com/gym/102697/problem/051">https://codeforces.com/gym/102697/problem/051</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int x1 = sc.nextInt();
        final int y1 = sc.nextInt();
        final int x2 = sc.nextInt();
        final int y2 = sc.nextInt();
        final int x3 = sc.nextInt();
        final int y3 = sc.nextInt();
        final int x4 = sc.nextInt();
        final int y4 = sc.nextInt();
        final Rectangle r1 = Rectangle.of(Math.min(x1, x2), Math.min(y1, y2),
                                          Math.max(x1, x2), Math.max(y1, y2));
        final Rectangle r2 = Rectangle.of(Math.min(x3, x4), Math.min(y3, y4),
                                          Math.max(x3, x4), Math.max(y3, y4));
        final String ans = r1.overlap(r2)
                    && !r1.inside(r2) && !r2.inside(r1)
                ? "YES" : "NO";
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

    private static class Rectangle {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private Rectangle(final int x1, final int y1, final int x2, final int y2) {
            if (x1 > x2 || y1 > y2) {
                throw new IllegalArgumentException();
            }
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public static Rectangle of(final int x1, final int y1, final int x2, final int y2) {
            return new Rectangle(x1, y1, x2, y2);
        }

        public boolean inside(final Rectangle other) {
            return this.x1 > other.x1 && this.x2 < other.x2
                    && this.y1 > other.y1 && this.y2 < other.y2;
        }

        public boolean overlap(final Rectangle other) {
            return this.overlapX(other) && this.overlapY(other);
        }

        private boolean overlapX(final Rectangle other) {
            return this.x1 >= other.x1 && this.x1 <= other.x2
                    || other.x1 >= this.x1 && other.x1 <= this.x2;
        }

        private boolean overlapY(final Rectangle other) {
            return this.y1 >= other.y1 && this.y1 <= other.y2
                    || other.y1 >= this.y1 && other.y1 <= this.y2;
        }
    }
}
