
package com.baidu.oped.apm.profiler.modifier.spring.beans;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.plugin.BytecodeUtils;
import com.baidu.oped.apm.profiler.DefaultAgent;
import com.baidu.oped.apm.profiler.modifier.method.interceptor.MethodInterceptor;
import com.baidu.oped.apm.profiler.modifier.spring.beans.BeanMethodModifier;
import com.baidu.oped.apm.test.MockAgent;

import org.junit.Test;

/**
 * class BeanMethodModifierTest 
 *
 * @author meidongxu@baidu.com
 */
public class BeanMethodModifierTest {

    private static final String TARGET = Maru.class.getName();
    private static final String TARGET_INTERNAL_NAME = TARGET.replace('.', '/');

    @Test
    public void test() throws Exception {
        DefaultAgent agent = MockAgent.of("pinpoint-spring-bean-test.config");
        ByteCodeInstrumentor realInstrumentor = agent.getByteCodeInstrumentor();

        final ClassLoader loader = getClass().getClassLoader();
        byte[] byteCode = BytecodeUtils.getClassFile(loader, TARGET);

        ByteCodeInstrumentor instrumentor = mock(ByteCodeInstrumentor.class);
        InstrumentClass instrumentClass = mock(InstrumentClass.class);
        
        when(instrumentor.getClass(loader, TARGET_INTERNAL_NAME, byteCode)).thenReturn(instrumentClass);
        when(instrumentClass.isInterceptable()).thenReturn(true);
        when(instrumentClass.getDeclaredMethods(BeanMethodModifier.METHOD_FILTER)).thenReturn(realInstrumentor.getClass(loader, TARGET, byteCode).getDeclaredMethods(BeanMethodModifier.METHOD_FILTER));

        BeanMethodModifier modifier = new BeanMethodModifier(instrumentor);
        modifier.modify(loader, TARGET_INTERNAL_NAME, null, byteCode);
        
                
        verify(instrumentor).getClass(loader, TARGET_INTERNAL_NAME, byteCode);

        verify(instrumentClass).isInterceptable();
        verify(instrumentClass).getDeclaredMethods(BeanMethodModifier.METHOD_FILTER);
        verify(instrumentClass).addInterceptor(eq("publicMethod"), eq(new String[0]), isA(MethodInterceptor.class));
        verify(instrumentClass).addInterceptor(eq("compareTo"), eq(new String[] { TARGET }), isA(MethodInterceptor.class));
        verify(instrumentClass).toBytecode();
        
        verifyNoMoreInteractions(instrumentor, instrumentClass);
    }
}
