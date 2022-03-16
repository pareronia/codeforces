package com.github.pareronia.codeforces.gym._102697._015;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;

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
import java.util.Objects;

/**
 * 015. Subway System
 * @see <a href="https://codeforces.com/gym/102697/problem/015">https://codeforces.com/gym/102697/problem/015</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private static class DFS {
        final Map<Station, List<Station>> adj;
        final Deque<Station> route;
        int ans;
        
        public DFS(final Map<Station, List<Station>> adj, final Station start) {
            this.adj = adj;
            this.route = new ArrayDeque<>(List.of(start));
            this.ans = Integer.MAX_VALUE;
        }

        public void dfs() {
            final int lines = (int) route.stream()
                    .map(Station::getLine).distinct().count();
            if (lines >= ans) {
                return;
            }
            final Station curr = route.peekLast();
            if ("Hotel".equals(curr.name)) {
                ans = Math.min(ans, lines);
                return;
            }
            for (final Station s : adj.get(curr)) {
                if (route.contains(s)) {
                    continue;
                }
                route.addLast(s);
                dfs();
                route.removeLast();
            }
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final Map<Station, List<Station>> adj = new HashMap<>();
        for (int j = 0; j < n; j++) {
            final String[] ss = sc.next().split(" ");
            for (int k = 1; k < ss.length; k++) {
                final Station s1 = Station.of(ss[k -1], j);
                final Station s2 = Station.of(ss[k], j);
                if (!adj.containsKey(s1)) {
                    adj.put(s1, new ArrayList<>());
                }
                if (!adj.containsKey(s2)) {
                    adj.put(s2, new ArrayList<>());
                }
                adj.get(s1).add(s2);
                adj.get(s2).add(s1);
            }
        }
        adj.keySet().stream().collect(groupingBy(Station::getName)).values().forEach(s -> {
            for (int j = 0; j < s.size(); j++) {
                for (int k = j + 1; k < s.size(); k++) {
                    adj.get(s.get(j)).add(s.get(k));
                    adj.get(s.get(k)).add(s.get(j));
                }
            }
        });
        final int ans = adj.keySet().stream()
                .filter(s -> "Airport".equals(s.name))
                .mapToInt(start -> {
                    final DFS dfs = new DFS(adj, start);
                    dfs.dfs();
                    return dfs.ans;
                })
                .min().orElseThrow();
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
    
    private static class Station {
        private final String name;
        private final int line;
        
        private Station(final String name, final int line) {
            this.name = name;
            this.line = line;
        }
        
        public static Station of(final String name, final int line) {
            return new Station(name, line);
        }

        public int getLine() {
            return line;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Station[name=").append(name).append(", line=")
                   .append(line).append("]");
            return builder.toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(line, name);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Station)) {
                return false;
            }
            final Station other = (Station) obj;
            return line == other.line && Objects.equals(name, other.name);
        }
    }
}
