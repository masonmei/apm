package com.baidu.oped.apm.bootstrap.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * class EqualsPathMatcherTest
 *
 * @author meidongxu@baidu.com
 */
public class EqualsPathMatcherTest {

    @Test
    public void testIsMatched() throws Exception {
        EqualsPathMatcher matcher = new EqualsPathMatcher("/test");
        Assert.assertTrue(matcher.isMatched("/test"));

        Assert.assertFalse(matcher.isMatched("/test/b"));

        Assert.assertFalse(matcher.isMatched(null));
        Assert.assertFalse(matcher.isMatched(""));
    }
}