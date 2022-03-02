package com.github.pareronia.codeforces._1216.c;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * C. White Sheet
 * @see <a href="https://codeforces.com/contest/1216/problem/C">https://codeforces.com/contest/1216/problem/C</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private Rectangle nextRectangle(final FastScanner sc) {
        return Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
    }
    
    private long ix(final Rectangle... rects) {
        return Rectangle.intersection(asList(rects))
                .map(Rectangle::getArea).orElse(0L);
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final Rectangle w = nextRectangle(sc);
        final Rectangle b1 = nextRectangle(sc);
        final Rectangle b2 = nextRectangle(sc);
        final int covered1 = (int) w.corners().stream()
            .filter(c -> b1.contains(c[0], c[1]))
            .count();
        final int covered2 = (int) w.corners().stream()
            .filter(c -> b2.contains(c[0], c[1]))
            .count();
        final String ans;
        if (covered1 == 4 || covered2 == 4
            || (covered1 == 2 && covered2 == 2
                && w.getArea() == ix(w, b1) + ix(w, b2) - ix(w, b1, b2)))
        {
            ans = "NO";
        } else {
            ans = "YES";
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (isSample()) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
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

        public long getArea() {
            return (long) Math.abs(x1 - x2) * (long) Math.abs(y1 - y2);
        }
        
        public Set<int[]> corners() {
            return Set.of(
                new int[] { x1, y1 },
                new int[] { x2, y2 },
                new int[] { x1, y2 },
                new int[] { x2, y1 }
            );
        }
        
        public boolean contains(final int x, final int y ) {
            return x1 <= x && x <= x2 && y1 <= y && y <= y2;
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

        public Optional<Rectangle> intersection(final Rectangle other) {
            if (!this.overlap(other)) {
                return Optional.empty();
            }
            return Optional.of(Rectangle.of(
                    Math.max(this.x1, other.x1),
                    Math.max(this.y1, other.y1),
                    Math.min(this.x2, other.x2),
                    Math.min(this.y2, other.y2)
            ));
        }
        
        public static Optional<Rectangle> intersection(final Collection<Rectangle> rects) {
            if (rects.size() < 2) {
                return Optional.empty();
            }
            final List<Rectangle> list = new ArrayList<>(rects);
            Optional<Rectangle> ix = list.get(1).intersection(list.get(0));
            for (int i = 2; i < list.size(); i++) {
                if (!ix.isPresent()) {
                    return Optional.empty();
                }
                ix = ix.get().intersection(list.get(i));
            }
            return ix;
        }
    }
}
