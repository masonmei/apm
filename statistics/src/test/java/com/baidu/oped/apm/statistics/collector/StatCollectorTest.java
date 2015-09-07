package com.baidu.oped.apm.statistics.collector;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.common.jpa.entity.Annotation;
import com.baidu.oped.apm.common.jpa.repository.AnnotationRepository;
import com.baidu.oped.apm.common.util.AnnotationTranscoder;
import com.baidu.oped.apm.statistics.Application;

/**
 * Created by mason on 9/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StatCollectorTest {

    @Autowired
    private StatCollector statCollector;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCollect() throws Exception {
        List<Annotation> all = annotationRepository.findAll();
        AnnotationTranscoder transcoder = new AnnotationTranscoder();

        all.stream().forEach(annotation -> {
            byte[] byteValue = annotation.getByteValue();
            byte valueType = annotation.getValueType();
            Object decode = transcoder.decode(valueType, byteValue);
            System.out.println(decode);
        });

//        statCollector.collect(1441343234250l, 3600000l);
    }
}