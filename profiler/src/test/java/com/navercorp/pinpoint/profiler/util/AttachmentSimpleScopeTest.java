
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.AttachmentFactory;
import com.baidu.oped.apm.bootstrap.instrument.AttachmentScope;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class AttachmentSimpleScopeTest 
 *
 * @author meidongxu@baidu.com
 */
public class AttachmentSimpleScopeTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void scopeCheck1() {
        AttachmentScope<String> scope = new AttachmentSimpleScope<String>("test");
        int push = scope.push();
        logger.debug("push:{}", push);

        Assert.assertNull(scope.getAttachment());
        scope.setAttachment("context");

        scope.push();

        Assert.assertEquals(scope.getAttachment(), "context");

        scope.pop();

        int pop = scope.pop();
        logger.debug("pop:{}", pop);

        Assert.assertNull(scope.getAttachment());

    }

    @Test
    public void scopeCheck2() {
        AttachmentScope<String> scope = new AttachmentSimpleScope<String>("test");
        scope.push();
        scope.setAttachment("context");
        scope.pop();

        scope.push();
        scope.push();

        scope.setAttachment("childContext");

        scope.pop();
        Assert.assertEquals(scope.getAttachment(), "childContext");
        scope.pop();
        Assert.assertNull(scope.getAttachment());
    }


    @Test
    public void getOrCreate() {
        AttachmentScope<String> scope = new AttachmentSimpleScope<String>("test");

        AttachmentFactory<String> factory = new AttachmentFactory<String>() {
            @Override
            public String createAttachment() {
                return "context";
            }
        };


        String orCreate = scope.getOrCreate(factory);
        Assert.assertEquals(orCreate, "context");
    }

}