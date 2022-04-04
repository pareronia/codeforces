package com.github.pareronia.codeforces.gym._102694.a;

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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A. Circumference of a Tree
 * @see <a href="https://codeforces.com/gym/102694/problem/A">https://codeforces.com/gym/102694/problem/A</a>
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
        final int n = sc.nextInt();
        final Adjacency[] adj = Adjacency.newAdjacencyList(n + 1);
        for (int j = 0; j < n - 1; j++) {
            final int v1 = sc.nextInt();
            final int v2 = sc.nextInt();
            adj[v1].add(v2);
            adj[v2].add(v1);
        }
        final int ans = Diameter.get(adj).dist * 3;
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
    
    private static class Diameter {
        private final int dist;
        
        private Diameter(final int dist) {
            this.dist = dist;
        }

        public static Diameter get(final Adjacency[] adj) {
            final int[] dist = new int[adj.length];
            final BFS bfsa = new BFS(adj, dist);
            bfsa.run(1);
            final BFS bfsb = new BFS(adj, dist);
            bfsb.run(bfsa.vmax);
            return new Diameter(bfsb.max);
        }
        
        private static class BFS {
            private final Adjacency[] adj;
            private int max;
            private int vmax;
            private final int[] dist;
            
            public BFS(final Adjacency[] adj, final int[] dist) {
                this.adj = adj;
                this.max = -2;
                this.dist = dist;
                Arrays.fill(this.dist, -1);
            }
            
            public void run(final int start) {
                final Deque<Integer> q = new ArrayDeque<>();
                dist[start] = 0;
                q.add(start);
                while (!q.isEmpty()) {
                    final int curr = q.poll();
                    if (dist[curr] > max) {
                        max = dist[curr];
                        vmax = curr;
                    }
                    for (final int n : adj[curr]) {
                        if (dist[n] == -1) {
                            dist[n] = dist[curr] + 1;
                            q.add(n);
                        }
                    }
                }
            }
        }
    }
    
    private static class Adjacency extends ArrayList<Integer> {
        private static final long serialVersionUID = 1L;
        
        public static Adjacency[] newAdjacencyList(final int n) {
            final Adjacency[] adj = new Adjacency[n];
            for (int j = 0; j < n; j++) {
                adj[j] = new Adjacency();
            }
            return adj;
        }
    }
}
