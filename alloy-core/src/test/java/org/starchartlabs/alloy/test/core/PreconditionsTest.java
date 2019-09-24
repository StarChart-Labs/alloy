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
    public void checkArgumentReturnValueStringNullMessage() throws Exception {
        Preconditions.checkArgument("value", null, "message");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "null")
    public void checkArgumentReturnValueStringFalseNull() throws Exception {
        Preconditions.checkArgument("value", t -> false, (Object) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentReturnValueStringFalseMessage() throws Exception {
        Preconditions.checkArgument("value", t -> false, "message");
    }

    @Test
    public void checkArgumentReturnValueNullTrueMessage() {
        Object result = Preconditions.checkArgument(null, t -> true, "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkArgumentReturnValueStringTrueMessage() {
        Object result = Preconditions.checkArgument("value", t -> true, "message");

        Assert.assertEquals(result, "value");
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

    @Test(expectedExceptions = NullPointerException.class)
    public void checkArgumentReturnValueStringNullPredicateSupplier() throws Exception {
        Preconditions.checkArgument("value", null, () -> "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkArgumentReturnValueStringNullSupplier() throws Exception {
        Preconditions.checkArgument("value", t -> false, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "null")
    public void checkArgumentReturnValueStringFalseNullMessageSupplier() throws Exception {
        Preconditions.checkArgument("value", t -> false, () -> null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentReturnValueStringFalseSupplier() throws Exception {
        Preconditions.checkArgument("value", t -> false, () -> "message");
    }

    @Test
    public void checkArgumentReturnValueNullTrueSupplier() throws Exception {
        Object result = Preconditions.checkArgument(null, t -> true, () -> "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkArgumentReturnValueStringTrueSupplier() throws Exception {
        Object result = Preconditions.checkArgument("value", t -> true, () -> "message");

        Assert.assertEquals(result, "value");
    }

    @Test
    public void checkArgumentTrueFormattedString() throws Exception {
        Preconditions.checkArgument(true, "%s", "message");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentFalseFormattedString() throws Exception {
        Preconditions.checkArgument(false, "%s", "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkArgumentReturnValueStringNullPredicateFormattedString() throws Exception {
        Preconditions.checkArgument("value", null, "%s", "message");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "message")
    public void checkArgumentReturnValueStringFalseFormattedString() throws Exception {
        Preconditions.checkArgument("value", t -> false, "%s", "message");
    }

    @Test
    public void checkArgumentReturnValueNullTrueFormattedString() throws Exception {
        Object result = Preconditions.checkArgument(null, t -> true, "%s", "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkArgumentReturnValueStringTrueFormattedString() throws Exception {
        Object result = Preconditions.checkArgument("value", t -> true, "%s", "message");

        Assert.assertEquals(result, "value");
    }

    @Test
    public void checkStateTrue() throws Exception {
        Preconditions.checkState(true);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void checkStateFalse() throws Exception {
        Preconditions.checkState(false);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkStateReturnValueStringNull() throws Exception {
        Preconditions.checkState("value", null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void checkStateReturnValueStringFalse() throws Exception {
        Preconditions.checkState("value", t -> false);
    }

    @Test
    public void checkStateReturnValueNullTrue() throws Exception {
        Object result = Preconditions.checkState(null, t -> true);

        Assert.assertNull(result);
    }

    @Test
    public void checkStateReturnValueStringTrue() throws Exception {
        String result = Preconditions.checkState("value", t -> true);

        Assert.assertEquals(result, "value");
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
    public void checkStateReturnValueStringNullMessage() throws Exception {
        Preconditions.checkState("value", null, "message");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "null")
    public void checkStateReturnValueStringFalseNull() throws Exception {
        Preconditions.checkState("value", t -> false, (Object) null);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateReturnValueStringFalseMessage() throws Exception {
        Preconditions.checkState("value", t -> false, "message");
    }

    @Test
    public void checkStateReturnValueNullTrueMessage() {
        Object result = Preconditions.checkState(null, t -> true, "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkStateReturnValueStringTrueMessage() {
        Object result = Preconditions.checkState("value", t -> true, "message");

        Assert.assertEquals(result, "value");
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

    @Test(expectedExceptions = NullPointerException.class)
    public void checkStateReturnValueStringNullPredicateSupplier() throws Exception {
        Preconditions.checkState("value", null, () -> "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkStateReturnValueStringNullSupplier() throws Exception {
        Preconditions.checkState("value", t -> false, null);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "null")
    public void checkStateReturnValueStringFalseSupplierNullMessage() throws Exception {
        Preconditions.checkState("value", t -> false, () -> null);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateReturnValueStringFalseSupplier() throws Exception {
        Preconditions.checkState("value", t -> false, () -> "message");
    }

    @Test
    public void checkStateReturnValueNullTrueSupplier() throws Exception {
        Object result = Preconditions.checkState(null, t -> true, () -> "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkStateReturnValueStringTrueSupplier() throws Exception {
        Object result = Preconditions.checkState("value", t -> true, () -> "message");

        Assert.assertEquals(result, "value");
    }

    @Test
    public void checkStateTrueFormattedString() throws Exception {
        Preconditions.checkState(true, "%s", "message");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateFalseFormattedString() throws Exception {
        Preconditions.checkState(false, "%s", "message");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkStateReturnValueStringNullPredicateFormattedString() throws Exception {
        Preconditions.checkState("value", null, "%s", "message");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "message")
    public void checkStateReturnValueStringFalseFormattedString() throws Exception {
        Preconditions.checkState("value", t -> false, "%s", "message");
    }

    @Test
    public void checkStateReturnValueNullTrueFormattedString() throws Exception {
        Object result = Preconditions.checkState(null, t -> true, "%s", "message");

        Assert.assertNull(result);
    }

    @Test
    public void checkStateReturnValueStringTrueFormattedString() throws Exception {
        Object result = Preconditions.checkState("value", t -> true, "%s", "message");

        Assert.assertEquals(result, "value");
    }

}
