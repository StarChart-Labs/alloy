/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import java.io.IOException;
import java.util.List;

import org.starchartlabs.alloy.core.Throwables;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ThrowablesTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void throwIfInstanceOfNullThrowable() throws Exception {
        Throwables.throwIfInstanceOf(null, IllegalArgumentException.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void throwIfInstanceNullDeclaredType() throws Exception {
        Throwables.throwIfInstanceOf(new IllegalArgumentException(), null);
    }

    @Test
    public void throwIfInstanceOfNotInstance() throws Exception {
        Throwables.throwIfInstanceOf(new IllegalArgumentException(), IllegalStateException.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throwIfInstanceOf() throws Exception {
        Throwables.throwIfInstanceOf(new IllegalArgumentException(), IllegalArgumentException.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void throwIfUncheckedNullThrowable() throws Exception {
        Throwables.throwIfUnchecked(null);
    }

    @Test
    public void throwIfUncheckedNotUnchecked() throws Exception {
        Throwables.throwIfUnchecked(new IOException());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void throwIfUncheckedRuntimeException() throws Exception {
        Throwables.throwIfUnchecked(new RuntimeException());
    }

    @Test(expectedExceptions = Error.class)
    public void throwIfUncheckedError() throws Exception {
        Throwables.throwIfUnchecked(new Error());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getRootCauseNullThrowable() throws Exception {
        Throwables.getRootCause(null);
    }

    @Test
    public void getRootCause() throws Exception {
        Throwable cause = new IllegalArgumentException();
        Throwable middle = new IllegalStateException(cause);
        Throwable top = new RuntimeException(middle);

        Throwable result = Throwables.getRootCause(top);

        Assert.assertEquals(result, cause);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getCausualChainNullThrowable() throws Exception {
        Throwables.getCausalChain(null);
    }

    @Test
    public void getCauseChain() throws Exception {
        Throwable cause = new IllegalArgumentException();
        Throwable middle = new IllegalStateException(cause);
        Throwable top = new RuntimeException(middle);

        List<Throwable> result = Throwables.getCausalChain(top);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.get(0), top);
        Assert.assertEquals(result.get(1), middle);
        Assert.assertEquals(result.get(2), cause);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getStackTraceAsStringNullThrowable() throws Exception {
        Throwables.getStackTraceAsString(null);
    }

    @Test
    public void getStackTraceAsString() throws Exception {
        Throwable cause = new IllegalArgumentException();
        Throwable middle = new IllegalStateException(cause);
        Throwable top = new RuntimeException(middle);

        String result = Throwables.getStackTraceAsString(top);

        for (StackTraceElement element : top.getStackTrace()) {
            Assert.assertTrue(result.contains(element.toString()));
        }
    }

}
