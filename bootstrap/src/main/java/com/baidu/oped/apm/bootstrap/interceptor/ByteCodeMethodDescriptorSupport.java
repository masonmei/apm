package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * this enables assigning "precompiled" methodDescriptor
 *
 * @author emeroad
 */
public interface ByteCodeMethodDescriptorSupport {
    void setMethodDescriptor(MethodDescriptor descriptor);
}
