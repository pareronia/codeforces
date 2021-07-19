package com.github.pareronia.codeforces;

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

import org.junit.jupiter.api.function.ThrowingConsumer;

public abstract class MainTestBase<T> {
	
	protected final Class<T> klass;

    protected MainTestBase(final Class<T> klass) {
		this.klass = klass;
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
