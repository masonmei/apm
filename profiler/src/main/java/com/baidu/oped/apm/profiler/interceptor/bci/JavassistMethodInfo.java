
package com.baidu.oped.apm.profiler.interceptor.bci;

import javassist.CtBehavior;
import javassist.CtConstructor;

import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.profiler.interceptor.DefaultMethodDescriptor;
import com.baidu.oped.apm.profiler.util.JavaAssistUtils;

/**
 * class JavassistMethodInfo 
 *
 * @author meidongxu@baidu.com
 */
public class JavassistMethodInfo implements MethodInfo {
    private final CtBehavior behavior;
    
    public JavassistMethodInfo(CtBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public String getName() {
        return behavior.getName();
    }

    @Override
    public String[] getParameterTypes() {
        return JavaAssistUtils.parseParameterSignature(behavior.getSignature());
    }

    @Override
    public int getModifiers() {
        return behavior.getModifiers();
    }
    
    @Override
    public boolean isConstructor() {
        return behavior instanceof CtConstructor;
    }

    @Override
    public MethodDescriptor getDescriptor() {
        String[] parameterVariableNames = JavaAssistUtils.getParameterVariableName(behavior);
        return new DefaultMethodDescriptor(behavior.getDeclaringClass().getName(), behavior.getName(), getParameterTypes(), parameterVariableNames);
    }

}
