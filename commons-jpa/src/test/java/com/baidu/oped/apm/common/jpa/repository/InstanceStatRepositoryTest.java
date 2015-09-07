package com.baidu.oped.apm.common.jpa.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.Application;

/**
 * Created by mason on 9/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class InstanceStatRepositoryTest {

    @Autowired
    private InstanceStatRepository instanceStatRepository;

    @Test
    public void testMinTimestamp() throws Exception {
        long minTimestamp = instanceStatRepository.minTimestamp();
        assertTrue(minTimestamp > 0);
    }
}