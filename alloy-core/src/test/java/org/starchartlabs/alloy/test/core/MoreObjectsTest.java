/*
 * Copyright (C) 2018 StarChart-Labs@github.com Authors
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package org.starchartlabs.alloy.test.core;

import org.starchartlabs.alloy.core.MoreObjects;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MoreObjectsTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void toStringHelperNullObject() throws Exception {
        MoreObjects.toStringHelper((Object) null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void toStringHelperNullClass() throws Exception {
        MoreObjects.toStringHelper((Class<?>) null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void toStringHelperNullString() throws Exception {
        MoreObjects.toStringHelper((String) null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void toStringHelperAddNullName() throws Exception {
        MoreObjects.toStringHelper("className")
        .add(null, "value");
    }

    @Test
    public void toStringHelperObject() throws Exception {
        Integer obj = Integer.valueOf(1);

        String result = MoreObjects.toStringHelper(obj)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{}");
    }

    @Test
    public void toStringHelperClass() throws Exception {
        String result = MoreObjects.toStringHelper(Integer.class)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{}");
    }

    @Test
    public void toStringHelperString() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{}");
    }

    @Test
    public void toStringHelperAddNullValue() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .add("one", "1")
                .add("two", null)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{one=1, two=null}");
    }

    @Test
    public void toStringHelperAddNullOmitNulls() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .omitNullValues()
                .add("one", "1")
                .add("two", null)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{one=1}");
    }

    @Test
    public void toStringHelperAdd() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .add("one", "1")
                .add("two", "2")
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{one=1, two=2}");
    }

    @Test
    public void toStringHelperAddValueNull() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .addValue("1")
                .addValue(null)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{1, null}");
    }

    @Test
    public void toStringHelperAddValueOmitNulls() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .omitNullValues()
                .addValue("1")
                .addValue(null)
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{1}");
    }

    @Test
    public void toStringHelperAddValue() throws Exception {
        String result = MoreObjects.toStringHelper("Integer")
                .addValue("1")
                .addValue("2")
                .toString();

        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Integer{1, 2}");
    }

}
