package com.github.pareronia.codeforces;

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

public abstract class MainTestBase<T> {
	
	protected final Class<T> klass;

    protected MainTestBase(final Class<T> klass) {
		this.klass = klass;
	}
    
    @TestFactory
    public Stream<DynamicTest> tests() throws URISyntaxException {
        return DynamicTest.stream(
                    allInputFiles(), displayNameGenerator(), testExecutor());
    }
    
    private Iterator<File> allInputFiles() throws URISyntaxException {
        final File folder
                = Paths.get(this.klass.getResource(".").toURI()).toFile();
        return Stream.of(folder.listFiles())
                .filter(f -> f.getName().endsWith("_input.txt"))
                .iterator();
    }

    private Function<File, String> displayNameGenerator() {
        return input -> input.getName();
    }
    
    private ThrowingConsumer<File> testExecutor() {
        return input -> {
            final List<String> result = run(new FileInputStream(input));
            System.out.println(input.getName());
            result.stream().forEach(System.out::println);
            System.out.println("");
            final Path outPath = outputForInput(input.toPath());
            assertThat(result, is(Files.readAllLines(outPath)));
        };
    }
    
    private Path outputForInput(final Path input) {
        final String outputFileName = StringUtils.substringBefore(
                        input.getFileName().toString(), "input.txt")
                + "output.txt";
        return input.getParent().resolve(outputFileName);
    }
 
	protected List<String> run(final InputStream in)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
    	
    	final Constructor<T> constructor
    			= this.klass.getDeclaredConstructor(
    			        Boolean.class, InputStream.class, PrintStream.class);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	final PrintStream out = new PrintStream(baos, true);
        final T solution = constructor.newInstance(FALSE, in, out);
    	final Method solve = this.klass.getDeclaredMethod("solve");
    	solve.invoke(solution);
        return asList(baos.toString().split("\\r?\\n"));
    }
	
	protected List<String> runWithTempFile(final ThrowingConsumer<BufferedWriter> writer) throws Throwable {
        final Path temp = createTempFile();
        try (final BufferedWriter theWriter = Files.newBufferedWriter(temp)) {
           writer.accept(theWriter);
        }
	    return run(Files.newInputStream(temp));
	}

    protected Path createTempFile() throws IOException {
        final FileAttribute<?>[] attrs = new FileAttribute[] {};
        final Path temp = Files.createTempFile(null, null, attrs);
        temp.toFile().deleteOnExit();
        return temp;
    }
}
