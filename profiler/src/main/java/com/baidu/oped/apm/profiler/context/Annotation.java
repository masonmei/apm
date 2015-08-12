
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.profiler.util.AnnotationValueMapper;
import com.baidu.oped.apm.thrift.dto.TAnnotation;
import com.baidu.oped.apm.thrift.dto.TAnnotationValue;
import com.baidu.oped.apm.thrift.dto.TIntStringStringValue;
import com.baidu.oped.apm.thrift.dto.TIntStringValue;

/**
 * class Annotation 
 *
 * @author meidongxu@baidu.com
 */
public class Annotation extends TAnnotation {

    public Annotation(int key) {
        super(key);
    }

    public Annotation(int key, Object value) {
        super(key);
        AnnotationValueMapper.mappingValue(this, value);
    }

    public Annotation(int key, TIntStringValue value) {
        super(key);
        this.setValue(TAnnotationValue.intStringValue(value));
    }

    public Annotation(int key, TIntStringStringValue value) {
        super(key);
        this.setValue(TAnnotationValue.intStringStringValue(value));
    }

    public Annotation(int key, String value) {
        super(key);
        this.setValue(TAnnotationValue.stringValue(value));
    }

    public Annotation(int key, int value) {
        super(key);
        this.setValue(TAnnotationValue.intValue(value));
    }

    public int getAnnotationKey() {
        return this.getKey();
    }

}
