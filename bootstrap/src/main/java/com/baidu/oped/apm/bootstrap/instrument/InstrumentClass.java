package com.baidu.oped.apm.bootstrap.instrument;

import java.util.List;

import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.TraceValue;

/**
 * class InstrumentClass
 *
 * @author meidongxu@baidu.com
 */
public interface InstrumentClass {

    boolean isInterface();

    String getName();

    String getSuperClass();

    String[] getInterfaces();

    @Deprecated
    boolean insertCodeBeforeMethod(String methodName, String[] args, String code);

    @Deprecated
    boolean insertCodeAfterMethod(String methodName, String[] args, String code);

    @Deprecated
    int addAllConstructorInterceptor(Interceptor interceptor) throws InstrumentException, NotFoundInstrumentException;

    @Deprecated
    int addAllConstructorInterceptor(Interceptor interceptor, Type type)
            throws InstrumentException, NotFoundInstrumentException;

    int addConstructorInterceptor(String[] args, Interceptor interceptor)
            throws InstrumentException, NotFoundInstrumentException;

    int addConstructorInterceptor(String[] args, Interceptor interceptor, Type type)
            throws InstrumentException, NotFoundInstrumentException;

    int reuseInterceptor(String methodName, String[] args, int interceptorId)
            throws InstrumentException, NotFoundInstrumentException;

    int reuseInterceptor(String methodName, String[] args, int interceptorId, Type type)
            throws InstrumentException, NotFoundInstrumentException;

    int addInterceptor(String methodName, String[] args, Interceptor interceptor)
            throws InstrumentException, NotFoundInstrumentException;

    int addScopeInterceptor(String methodName, String[] args, Interceptor interceptor, String scopeName)
            throws InstrumentException, NotFoundInstrumentException;

    int addScopeInterceptor(String methodName, String[] args, Interceptor interceptor, ScopeDefinition scopeDefinition)
            throws InstrumentException;

    int addScopeInterceptorIfDeclared(String methodName, String[] args, Interceptor interceptor, String scopeName)
            throws InstrumentException;

    int addScopeInterceptorIfDeclared(String methodName, String[] args, Interceptor interceptor,
                                      ScopeDefinition scopeDefinition) throws InstrumentException;

    int addInterceptor(String methodName, String[] args, Interceptor interceptor, Type type)
            throws InstrumentException, NotFoundInstrumentException;

    void weaving(String adviceClassName, ClassLoader classLoader) throws InstrumentException;

    boolean addDebugLogBeforeAfterMethod();

    boolean addDebugLogBeforeAfterConstructor();

    byte[] toBytecode() throws InstrumentException;

    Class<?> toClass() throws InstrumentException;

    /**
     * Use addTraceValue instead of this method.
     */
    @Deprecated
    void addTraceVariable(String variableName, String setterName, String getterName, String variableType,
                          String initValue) throws InstrumentException;

    /**
     * Use addTraceValue instead of this method.
     */
    @Deprecated
    void addTraceVariable(String variableName, String setterName, String getterName, String variableType)
            throws InstrumentException;

    void addTraceValue(Class<? extends TraceValue> traceValue, String initValue) throws InstrumentException;

    void addTraceValue(Class<? extends TraceValue> traceValue) throws InstrumentException;

    boolean insertCodeAfterConstructor(String[] args, String code);

    boolean insertCodeBeforeConstructor(String[] args, String code);

    List<MethodInfo> getDeclaredMethods();

    List<MethodInfo> getDeclaredMethods(MethodFilter methodFilter);

    MethodInfo getDeclaredMethod(String name, String[] parameterTypes);

    MethodInfo getConstructor(String[] parameterTypes);

    public boolean isInterceptable();

    boolean hasDeclaredMethod(String methodName, String[] args);

    boolean hasMethod(String methodName, String[] parameterTypeArray, String returnType);

    InstrumentClass getNestedClass(String className);

    void addGetter(String getterName, String fieldName, String fieldType) throws InstrumentException;
}
