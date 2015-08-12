package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class AttachmentFactory
 *
 * @author meidongxu@baidu.com
 */
public interface AttachmentFactory<T> {
    T createAttachment();
}
