/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import java.util.function.Supplier;

import org.starchartlabs.alloy.core.Preconditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PreconditionsTest {

    @Test
    public void checkArgumentTrue() throws Exception {
        Preconditions.checkArgument(true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkArgumentFalse() throws Exception {
        Preconditions.checkArgument(false);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkArgumentReturnValueStringNull() throws Exception {
        Preconditions.checkArgument("value", null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkArgumentReturnValueStringFalse() throws Exception {
        Preconditions.checkArgument("value", t -> false);
    }

    @Test
    public void checkArgumentReturnValueNullTrue() throws Exception {
        Object result = Preconditions.checkArgument(null, t -> true);

        Assert.assertNull(result);
    }

    @Test
    public void checkArgumentReturnValueStringTrue() throws Exception {
        String result = Preconditions.checkArgument("value", t -> true);

        Assert.assertEquals(result, "value");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "null")
    public void checkArgumentFalseNullMessage() throws Exception {
        Preconditions.checkArgument(false, (Object) null);
    }

    @Test
    public void checkArgumentTrueMessage() throws Exception {
        Preconditions.checkArgument(true, "message");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentFalseMessage() throws Exception {
        Preconditions.checkArgument(false, "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkArgumentNullSupplier() throws Exception {
        Preconditions.checkArgument(true, (Supplier<String>) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "null")
    public void checkArgumentFalseSupplierNullMessage() throws Exception {
        Preconditions.checkArgument(false, () -> null);
    }

    @Test
    public void checkArgumentTrueSupplier() throws Exception {
        Preconditions.checkArgument(true, () -> "message");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentFalseSupplier() throws Exception {
        Preconditions.checkArgument(false, () -> "message");
    }

    @Test
    public void checkStateTrue() throws Exception {
        Preconditions.checkState(true);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void checkStateFalse() throws Exception {
        Preconditions.checkState(false);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "null")
    public void checkStateFalseNullMessage() throws Exception {
        Preconditions.checkState(false, (Object) null);
    }

    @Test
    public void checkStateTrueMessage() throws Exception {
        Preconditions.checkState(true, "message");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateFalseMessage() throws Exception {
        Preconditions.checkState(false, "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkStateNullSupplier() throws Exception {
        Preconditions.checkState(true, (Supplier<String>) null);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "null")
    public void checkStateFalseSupplierNullMessage() throws Exception {
        Preconditions.checkState(false, () -> null);
    }

    @Test
    public void checkStateTrueSupplier() throws Exception {
        Preconditions.checkState(true, () -> "message");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateFalseSupplier() throws Exception {
        Preconditions.checkState(false, () -> "message");
    }

}
