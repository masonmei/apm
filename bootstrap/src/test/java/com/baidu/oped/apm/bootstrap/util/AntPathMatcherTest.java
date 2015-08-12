package com.baidu.oped.apm.bootstrap.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * class AntPathMatcherTest
 *
 * @author meidongxu@baidu.com
 */
public class AntPathMatcherTest {
    @Test
    public void isAntStyle() {
        Assert.assertTrue(AntPathMatcher.isAntStylePattern("/*/test"));
        Assert.assertTrue(AntPathMatcher.isAntStylePattern("/*/?"));

        Assert.assertFalse(AntPathMatcher.isAntStylePattern("/abc/test"));
    }

    @Test
    public void isMatched() {

        AntPathMatcher matcher = new AntPathMatcher("/test/?bc");
        Assert.assertTrue(matcher.isMatched("/test/abc"));

        Assert.assertFalse(matcher.isMatched("/test/axx"));

        Assert.assertFalse(matcher.isMatched(null));
        Assert.assertFalse(matcher.isMatched(""));
        Assert.assertFalse(matcher.isMatched("test"));
    }

}