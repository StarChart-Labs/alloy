/*
 * Copyright (C) 2018 StarChart Labs Authors.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core.collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.starchartlabs.alloy.core.collections.MoreCollections;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MoreCollectionsTest {

    @Test
    public void mapBuilderSupplier() throws Exception {
        Map<String, String> result = MoreCollections.<String, String> mapBuilder()
                .mutable()
                .put("key", "value")
                .build(LinkedHashMap::new);

        Assert.assertTrue(result instanceof LinkedHashMap, "Result was " + result.getClass().getSimpleName());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get("key"), "value");
    }

    @Test
    public void mapBuilder() throws Exception {
        Map<String, String> result = MoreCollections.<String, String> mapBuilder()
                .put("key", "value")
                .build();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get("key"), "value");

        // Check that we got an unmodifiable map back
        try {
            result.put("key2", "value2");
            Assert.fail("Modification was allowed on result");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void mapBuilderPutAll() throws Exception {
        Map<String, String> source = new HashMap<>();
        source.put("key", "value");

        Map<String, String> result = MoreCollections.<String, String> mapBuilder()
                .putAll(source)
                .build();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get("key"), "value");

        // Check that we got an unmodifiable map back
        try {
            result.put("key2", "value2");
            Assert.fail("Modification was allowed on result");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void mapBuilderUnmodifiable() throws Exception {
        Map<String, String> result = MoreCollections.<String, String> mapBuilder()
                .mutable()
                .put("key", "value")
                .build();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get("key"), "value");

        result.put("key2", "value2");

        Assert.assertEquals(result.size(), 2);
        Assert.assertEquals(result.get("key2"), "value2");
    }

    @Test
    public void mapBuilderAcceptNullValue() throws Exception {
        Map<String, String> result = MoreCollections.<String, String> mapBuilder()
                .put("key", null)
                .build();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertNull(result.get("key"));

        // Check that we got an unmodifiable map back
        try {
            result.put("key2", "value2");
            Assert.fail("Modification was allowed on result");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }
    }

}
