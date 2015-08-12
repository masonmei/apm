package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class Attachment
 *
 * @author meidongxu@baidu.com
 */
public interface Attachment<T> {

    T getAttachment();

    void setAttachment(T object);

    T getOrCreate(AttachmentFactory<T> attachmentFactory);
}
