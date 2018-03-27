/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.starchartlabs.alloy.core.Suppliers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SuppliersTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void mapNullSupplier() throws Exception {
        Suppliers.map(null, a -> a);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void mapNullFunction() throws Exception {
        Suppliers.map(() -> 1, null);
    }

    @Test
    public void map() throws Exception {
        CountingSupplier supplier = new CountingSupplier();

        Supplier<String> result = Suppliers.map(supplier, a -> a.toString());

        Assert.assertNotNull(result);
        Assert.assertEquals(supplier.getCount(), 0);

        String output = result.get();

        Assert.assertEquals(output, "1");
        Assert.assertEquals(supplier.getCount(), 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void memoizeNullDelegate() throws Exception {
        Suppliers.memoize(null);
    }

    @Test
    public void memoize() throws Exception {
        CountingSupplier supplier = new CountingSupplier();

        Supplier<Integer> result = Suppliers.memoize(supplier);

        Assert.assertNotNull(result);

        // Should always result in 1, as thats the first result and will be cached
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(result.get().intValue(), 1);
        }

        // Should only have called the delegate once
        Assert.assertEquals(supplier.getCount(), 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void memoizeWithExpirationNullDelegate() throws Exception {
        Suppliers.memoizeWithExpiration(null, 1, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void memoizeWithExpirationNegativeDuration() throws Exception {
        Suppliers.memoizeWithExpiration(() -> 1, -1, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void memoizeWithExpirationNullTimeUnit() throws Exception {
        Suppliers.memoizeWithExpiration(() -> 1, 1, null);
    }

    @Test
    public void memoizeWithExpiration() throws Exception {
        CountingSupplier supplier = new CountingSupplier();

        Supplier<Integer> result = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        Assert.assertNotNull(result);

        // This takes less than 10 milliseconds start to end
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(result.get().intValue(), 1);
        }

        // Let the expiration happen
        Thread.sleep(11);

        // SHould now advance with second call
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(result.get().intValue(), 2);
        }

        // Delegate was called twice - once for first expiration, once for second
        Assert.assertEquals(supplier.getCount(), 2);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void synchronizedSupplierNullDelegate() throws Exception {
        Suppliers.synchronizedSupplier(null);
    }

    @Test
    public void synchronizedSupplier() throws Exception {
        Supplier<Integer> result = Suppliers.synchronizedSupplier(() -> 1);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.get().intValue(), 1);
    }

    /**
     * Test supplier which allows checking the number of times the {@code get} method was called
     */
    private static class CountingSupplier implements Supplier<Integer> {

        private int count = 0;

        @Override
        public Integer get() {
            return ++count;
        }

        public int getCount() {
            return count;
        }

    }
}
