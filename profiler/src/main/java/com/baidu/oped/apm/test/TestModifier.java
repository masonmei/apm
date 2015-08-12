
package com.baidu.oped.apm.test;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * class TestModifier 
 *
 * @author meidongxu@baidu.com
 */
public abstract class TestModifier extends AbstractModifier {

    private String targetClass;

    public final List<Interceptor> interceptorList = new ArrayList<Interceptor>();

    public TestModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }


    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public String getTargetClass() {
        return targetClass;
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptorList.add(interceptor);
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public Interceptor getInterceptor(int index) {
        return interceptorList.get(index);
    }




    @Override
    public abstract byte[] modify(ClassLoader classLoader, String className, ProtectionDomain protectedDomain, byte[] classFileBuffer);


}
