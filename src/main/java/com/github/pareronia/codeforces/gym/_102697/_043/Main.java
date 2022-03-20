package com.github.pareronia.codeforces.gym._102697._043;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 043. Low-Budget Flight Paths
 * @see <a href="https://codeforces.com/gym/102697/problem/043">https://codeforces.com/gym/102697/problem/043</a>
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
        final String s = sc.next();
        final int n = sc.nextInt();
        final int b = sc.nextInt();
        final Map<String, List<Flight>> adj = new HashMap<>();
        for (int j = 0; j < n; j++) {
            final String from = sc.next();
            final String to = sc.next();
            final int cost = sc.nextInt();
            if (!adj.containsKey(from)) {
                adj.put(from, new ArrayList<>());
            }
            adj.get(from).add(Flight.to(to, cost));
        }
        final DFS dfs = new DFS(adj, s, b);
        dfs.dfs();
        String ans;
        if (dfs.best == Integer.MAX_VALUE) {
            ans = "IMPOSSIBLE";
        } else {
            final List<String> a = new ArrayList<>();
            a.add(String.format("%d %d", dfs.path.size() - 1, dfs.best));
            for (int j = 1; j < dfs.path.size(); j++) {
                a.add(dfs.path.get(j - 1) + " -> "+ dfs.path.get(j));
            }
            ans = a.stream().collect(joining(System.lineSeparator()));
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
    
    private static class DFS {
        private final Map<String, List<Flight>> adj;
        private final int budget;
        private final String destination;
        private final String start;
        private final Deque<String> stack;
        private int cost;
        private int best;
        private List<String> path;
        
        public DFS(final Map<String, List<Flight>> adj, final String destination, final int budget) {
            this.adj = adj;
            this.destination = destination;
            this.budget = budget;
            this.start = "Syracuse";
            this.stack = new ArrayDeque<>(List.of(start));
            this.cost = 0;
            this.best = Integer.MAX_VALUE;
        }

        public void dfs() {
            if (cost > budget) {
                return;
            }
            final String curr = stack.peekLast();
            if (stack.size() > 1
                    && start.equals(curr)
                    && stack.contains(destination)) {
                if (cost < best) {
                    best = cost;
                    path = stack.stream().collect(toList());
                }
                return;
            }
            for (final Flight flight : adj.getOrDefault(curr, emptyList())) {
                stack.addLast(flight.city);
                cost += flight.cost;
                dfs();
                stack.removeLast();
                cost -= flight.cost;
            }
        }
    }
    
    private static class Flight {
        private final String city;
        private final int cost;
        
        private Flight(final String city, final int cost) {
            this.city = city;
            this.cost = cost;
        }

        public static Flight to(final String city, final int cost) {
            return new Flight(city, cost);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Flight[city=").append(city).append(", cost=")
                .append(cost).append("]");
            return builder.toString();
        }
    }
}
