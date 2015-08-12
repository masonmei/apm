package com.baidu.oped.apm.bootstrap.config;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class ExcludeUrlFilterTest
 *
 * @author meidongxu@baidu.com
 */
public class ExcludeUrlFilterTest {

    @Test
    public void testFilter() throws Exception {
        Filter<String> filter = new ExcludeUrlFilter("/monitor/l7check.html, test/l4check.html");

        assertFilter(filter);
    }

    @Test
    public void testFilter_InvalidExcludeURL() throws Exception {
        Filter<String> filter = new ExcludeUrlFilter("/monitor/l7check.html, test/l4check.html, ,,");

        assertFilter(filter);
    }

    @Test
    public void testFilter_emptyExcludeURL() throws Exception {
        Filter<String> filter = new ExcludeUrlFilter("");

        Assert.assertFalse(filter.filter("/monitor/l7check.html"));
        Assert.assertFalse(filter.filter("test/l4check.html"));

        Assert.assertFalse(filter.filter("test/"));
        Assert.assertFalse(filter.filter("test/l4check.htm"));
    }

    private void assertFilter(Filter<String> filter) {
        Assert.assertTrue(filter.filter("/monitor/l7check.html"));
        Assert.assertTrue(filter.filter("test/l4check.html"));

        Assert.assertFalse(filter.filter("test/"));
        Assert.assertFalse(filter.filter("test/l4check.htm"));

        Assert.assertFalse(filter.filter(null));
        Assert.assertFalse(filter.filter(""));
    }

    @Test
    public void antStylePath() throws Exception {
        Filter<String> filter = new ExcludeUrlFilter("/monitor/l7check.*,/*/l7check.*");

        Assert.assertTrue(filter.filter("/monitor/l7check.jsp"));
        Assert.assertTrue(filter.filter("/monitor/l7check.html"));

        Assert.assertFalse(filter.filter("/monitor/test.jsp"));

        Assert.assertTrue(filter.filter("/*/l7check.html"));

        Assert.assertFalse(filter.filter(null));
        Assert.assertFalse(filter.filter(""));
    }

    @Test
    public void antstyle_equals_match() throws Exception {
        Filter<String> filter = new ExcludeUrlFilter("/monitor/stringEquals,/monitor/antstyle.*");

        Assert.assertTrue(filter.filter("/monitor/stringEquals"));
        Assert.assertTrue(filter.filter("/monitor/antstyle.html"));

        Assert.assertFalse(filter.filter("/monitor/stringEquals.test"));
        Assert.assertFalse(filter.filter("/monitor/antstyleXXX.html"));
    }
}