package com.github.pareronia.codeforces.gym._103647.d;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * D. Parrot Riddles
 * @see <a href="https://codeforces.com/gym/103647/problem/D">https://codeforces.com/gym/103647/problem/D</a>
 */
public class Main {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    private final int number;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out, final int number) {
        this.sample = sample;
        this.in = in;
        this.out = out;
        this.number = number;
    }
    
    private void handleTestCase(final FastScanner sc) {
        final boolean div2 = "yes".equals(query(sc, 2));
        final boolean div3 = "yes".equals(query(sc, 3));
        final boolean div4 = "yes".equals(query(sc, 4));
        final boolean div5 = "yes".equals(query(sc, 5));
        final boolean div7 = "yes".equals(query(sc, 7));
        final boolean div9 = "yes".equals(query(sc, 9));
        final boolean div11 = "yes".equals(query(sc, 11));
        final boolean div13 = "yes".equals(query(sc, 13));
        final boolean div17 = "yes".equals(query(sc, 17));
        final boolean div19 = "yes".equals(query(sc, 19));
        final boolean div23 = "yes".equals(query(sc, 23));
        final boolean div25 = "yes".equals(query(sc, 25));
        final boolean div29 = "yes".equals(query(sc, 29));
        final boolean div31 = "yes".equals(query(sc, 31));
        final boolean div37 = "yes".equals(query(sc, 37));
        final boolean div41 = "yes".equals(query(sc, 41));
        final boolean div43 = "yes".equals(query(sc, 43));
        final boolean div47 = "yes".equals(query(sc, 47));
        final boolean div49 = "yes".equals(query(sc, 49));
        if (div2 && div3) {
            answer(sc, "composite");
        } else if (div4 || div9 || div25 || div49) {
            answer(sc, "composite");
        } else if (div2 && div5) {
            answer(sc, "composite");
        } else if (div2 && div7) {
            answer(sc, "composite");
        } else if (div3 && div5) {
            answer(sc, "composite");
        } else if (div3 && div7) {
            answer(sc, "composite");
        } else if (div2 && div11) {
            answer(sc, "composite");
        } else if (div2 && div13) {
            answer(sc, "composite");
        } else if (div3 && div11) {
            answer(sc, "composite");
        } else if (div2 && div17) {
            answer(sc, "composite");
        } else if (div5 && div7) {
            answer(sc, "composite");
        } else if (div2 && div19) {
            answer(sc, "composite");
        } else if (div3 && div13) {
            answer(sc, "composite");
        } else if (div2 && div23) {
            answer(sc, "composite");
        } else if (div3 && div17) {
            answer(sc, "composite");
        } else if (div5 && div11) {
            answer(sc, "composite");
        } else if (div3 && div19) {
            answer(sc, "composite");
        } else if (div2 && div29) {
            answer(sc, "composite");
        } else if (div2 && div31) {
            answer(sc, "composite");
        } else if (div5 && div13) {
            answer(sc, "composite");
        } else if (div3 && div23) {
            answer(sc, "composite");
        } else if (div2 && div37) {
            answer(sc, "composite");
        } else if (div7 && div11) {
            answer(sc, "composite");
        } else if (div2 && div41) {
            answer(sc, "composite");
        } else if (div5 && div17) {
            answer(sc, "composite");
        } else if (div2 && div43) {
            answer(sc, "composite");
        } else if (div3 && div29) {
            answer(sc, "composite");
        } else if (div7 && div13) {
            answer(sc, "composite");
        } else if (div3 && div31) {
            answer(sc, "composite");
        } else if (div2 && div47) {
            answer(sc, "composite");
        } else if (div5 && div19) {
            answer(sc, "composite");
        } else {
            answer(sc, "prime");
        }
    }
    
    private String query(final FastScanner sc, final int n) {
        if (sample) {
            return this.number % n == 0 ? "yes" : "no";
        } else {
            this.out.println("? " + n);
            this.out.flush();
            return sc.next();
        }
    }
    
    private void answer(final FastScanner sc, final String s) {
        if (sample) {
            if (!BigInteger.valueOf(number).isProbablePrime(10) && "prime".equals(s)) {
                throw new AssertionError();
            }
        } else {
            this.out.println("! " + s);
            this.out.flush();
        }
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            handleTestCase(sc);
        }
    }

    public static void main(final String[] args) {
        new Main(false, System.in, System.out, -1).solve();
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
