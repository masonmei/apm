
package com.baidu.oped.apm.profiler.modifier.spring.beans;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baidu.oped.apm.profiler.ClassFileRetransformer;
import com.baidu.oped.apm.profiler.DefaultAgent;
import com.baidu.oped.apm.profiler.modifier.Modifier;
import com.baidu.oped.apm.profiler.modifier.spring.beans.AbstractAutowireCapableBeanFactoryModifier;
import com.baidu.oped.apm.test.ClassTransformHelper;
import com.baidu.oped.apm.test.MockAgent;

/**
 * class AbstractAutowireCapableBeanFactoryModifierTest 
 *
 * @author meidongxu@baidu.com
 */
public class AbstractAutowireCapableBeanFactoryModifierTest {

    @Test
    public void test() throws Exception {
        DefaultAgent agent = MockAgent.of("pinpoint-spring-bean-test.config");
        ClassFileRetransformer retransformer = mock(ClassFileRetransformer.class);
        Modifier beanModifier = mock(Modifier.class);
        
        AbstractAutowireCapableBeanFactoryModifier modifier = AbstractAutowireCapableBeanFactoryModifier.of(agent.getByteCodeInstrumentor(), agent, retransformer, beanModifier);

        
        ClassLoader loader = getClass().getClassLoader();
        ClassTransformHelper.transformClass(loader, "org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory", modifier);
        
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans-test.xml");
        
        verify(retransformer).retransform(Maru.class, beanModifier);
        verify(retransformer).retransform(Morae.class, beanModifier);
        verify(retransformer).retransform(Outer.class, beanModifier);
        verify(retransformer).retransform(Inner.class, beanModifier);
        verify(retransformer).retransform(ProxyTarget.class, beanModifier);
        verifyNoMoreInteractions(retransformer);

        context.getBean("mozzi");
        context.getBean("mozzi");
        
        verify(retransformer).retransform(Mozzi.class, beanModifier);
        verifyNoMoreInteractions(retransformer);
        
        assertFalse(ProxyTarget.class.equals(context.getBean("proxyTarget").getClass()));
    }
}
