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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Marc Klinger - mklinger[at]mklinger[dot]de
 */
public class ThrowablesTest {
	@Test
	public void stackTraceToStringTest() {
		final String s = Throwables.stackTraceToString(new Exception("blabla"));

		assertThat(s, containsString(getClass().getName()));
		assertThat(s, containsString("\tat"));
		assertThat(s, containsString(Exception.class.getName()));
		assertThat(s, containsString("blabla"));
	}

	@Test
	public void firstCauseTest() {
		final Exception e5 = new UnsupportedOperationException("e5");
		final Exception e4 = new IllegalArgumentException("e4", e5);
		final Exception e3 = new UnsupportedOperationException("e3", e4);
		final Exception e2 = new IllegalArgumentException("e2", e3);
		final Exception e1 = new Exception("e1", e2);

		assertThat(Throwables.firstCause(e1, Exception.class).get(), sameInstance(e1));
		assertThat(Throwables.firstCause(e1, Throwable.class).get(), sameInstance(e1));
		assertThat(Throwables.firstCause(e1, NullPointerException.class).orElse(null), nullValue());

		assertThat(Throwables.firstCause(e1, IllegalArgumentException.class).get(), sameInstance(e2));
		assertThat(Throwables.firstCause(e1, UnsupportedOperationException.class).get(), sameInstance(e3));

		assertThat(Throwables.firstCause(e3, IllegalArgumentException.class).get(), sameInstance(e4));
		assertThat(Throwables.firstCause(e3, Throwable.class).get(), sameInstance(e3));

		assertThat(Throwables.firstCause(e4, UnsupportedOperationException.class).get(), sameInstance(e5));
		assertThat(Throwables.firstCause(e4, Throwable.class).get(), sameInstance(e4));

		assertThat(Throwables.firstCause(null, Throwable.class).orElse(null), nullValue());
	}

	@Test
	public void hasCauseTest() {
		final Exception e5 = new UnsupportedOperationException("e5");
		final Exception e4 = new IllegalArgumentException("e4", e5);
		final Exception e3 = new UnsupportedOperationException("e3", e4);
		final Exception e2 = new IllegalArgumentException("e2", e3);
		final Exception e1 = new Exception("e1", e2);

		assertThat(Throwables.hasCause(e1, Exception.class), is(true));
		assertThat(Throwables.hasCause(e1, Throwable.class), is(true));
		assertThat(Throwables.hasCause(e1, NullPointerException.class), is(false));

		assertThat(Throwables.hasCause(e1, IllegalArgumentException.class), is(true));
		assertThat(Throwables.hasCause(e1, UnsupportedOperationException.class), is(true));

		assertThat(Throwables.hasCause(e3, IllegalArgumentException.class), is(true));
		assertThat(Throwables.hasCause(e3, Throwable.class), is(true));

		assertThat(Throwables.hasCause(e4, UnsupportedOperationException.class), is(true));
		assertThat(Throwables.hasCause(e4, Throwable.class), is(true));

		assertThat(Throwables.hasCause(null, Throwable.class), is(false));
	}
}
