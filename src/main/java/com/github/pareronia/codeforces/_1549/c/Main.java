package com.github.pareronia.codeforces._1549.c;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * C. Web of Lies
 * @see <a href="https://codeforces.com/contest/1549/problem/C">https://codeforces.com/contest/1549/problem/C</a>
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
    
    private final static class MutableSet {
        public static Set<Integer> of(final Integer i) {
            return new HashSet<>(Set.of(i));
        }
        
        public static Set<Integer> merge(final Set<Integer> s1, final Set<Integer> s2) {
            s1.addAll(s2);
            return s1;
        }
    }
    
    private Result<?> handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int m = sc.nextInt();
        final Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int j = 0; j < m; j++) {
            final int a = sc.nextInt();
            final int b = sc.nextInt();
            map.merge(a, MutableSet.of(b), MutableSet::merge);
            map.merge(b, MutableSet.of(a), MutableSet::merge);
        }
        final int q = sc.nextInt();
        final List<Long> ans = new ArrayList<>();
        final Map<Integer, Set<Integer>> addMap = new HashMap<>();
        final Map<Integer, Set<Integer>> delMap = new HashMap<>();
        for (int j = 0; j < q; j++) {
            final int t = sc.nextInt();
            if (t == 1) {
                final int u = sc.nextInt();
                final int v = sc.nextInt();
                addMap.merge(u, MutableSet.of(v), MutableSet::merge);
                addMap.merge(v, MutableSet.of(u), MutableSet::merge);
            } else if (t == 2) {
                final int u = sc.nextInt();
                final int v = sc.nextInt();
                delMap.merge(u, MutableSet.of(v), MutableSet::merge);
                delMap.merge(v, MutableSet.of(u), MutableSet::merge);
            } else {
                final Set<Integer> vulnerable = new HashSet<>();
                while (true) {
                    final Set<Integer> newVulnerable
                        = Stream.iterate(1, k -> k <= n, k -> k + 1)
                            .filter(x -> !vulnerable.contains(x))
                            .filter(x -> {
                                final Set<Integer> friends
                                        = newFriends(x, map, addMap, delMap);
                                return isVulnerable(x, friends, vulnerable);
                            })
                            .collect(toSet());
                    if (newVulnerable.isEmpty()) {
                        break;
                    }
                    vulnerable.addAll(newVulnerable);
                }
                ans.add(Stream.iterate(1, k -> k <= n, k -> k + 1)
                        .filter(x -> !vulnerable.contains(x))
                        .count());
                addMap.clear();
                delMap.clear();
            }
        }
        return new Result<>(i, ans);
    }
    
    private Set<Integer> newFriends(
            final Integer x,
            final Map<Integer, Set<Integer>> map,
            final Map<Integer, Set<Integer>> addMap,
            final Map<Integer, Set<Integer>> delMap
            ) {
        final Set<Integer> friends = new HashSet<>();
        friends.addAll(map.getOrDefault(x, emptySet()));
        friends.addAll(addMap.getOrDefault(x, emptySet()));
        friends.removeAll(delMap.getOrDefault(x, emptySet()));
        return friends;
    }

    private boolean isVulnerable(
            final Integer x,
            final Set<Integer> friends,
            final Set<Integer> vulnerable
            ) {
        final Set<Integer> f = friends.stream()
                .filter(y -> !vulnerable.contains(y))
                .collect(toSet());
        if (f.isEmpty()) {
            return false;
        }
        return f.stream().allMatch(y -> y > x);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (this.sample) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
            final List<Result<?>> results
                    = Stream.iterate(1, i -> i <= numberOfTestCases, i -> i + 1)
                            .map(i -> handleTestCase(i, sc))
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
