/*
 * Copyright mklinger GmbH - https://www.mklinger.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mklinger.micro.throwables;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Utility class for Throwables.
 *
 * @author Marc Klinger - mklinger[at]mklinger[dot]de
 */
public class Throwables {
	/** No instantiation */
	private Throwables() {}

	/**
	 * Get the stack trace of the given Throwable as string.
	 * @param e The throwable
	 * @return The stack strace as string
	 */
	public static String stackTraceToString(final Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	/**
	 * Find the first occurency of the given error type in the cause chain.
	 * <p>
	 * If {@code e} is of type {@code errorType}, return an Optional containing e.
	 * Otherwise traverse the causes until a cause of type {@code errorType} is
	 * found and return an Optional containing this instance. If no instance in the
	 * cause chain is of type {@code errorType}, return an empty optional.
	 * </p>
	 *
	 * @param e A Throwable
	 * @param errorType The type to find
	 * @return An Optional containing the first occurrence of the given type in the
	 *         cause chain, <code>null</code> if no such element exists
	 */
	public static <T extends Throwable> Optional<T> firstCause(final Throwable e, final Class<T> errorType) {
		if (e == null) {
			return Optional.empty();
		}
		if (errorType.isAssignableFrom(e.getClass())) {
			return Optional.of(errorType.cast(e));
		} else {
			return firstCause(e.getCause(), errorType);
		}
	}

	/**
	 * Check for the given exception or one of its causes being of the given type.
	 *
	 * @param e A Throwable
	 * @param errorType The type to find
	 * @return <code>true</code> if the given exception is of the given error type
	 *         or there is any occurrence of the given error type in the cause chain
	 */
	public static boolean hasCause(final Throwable e, final Class<? extends Throwable> errorType) {
		return firstCause(e, errorType).isPresent();
	}
}
