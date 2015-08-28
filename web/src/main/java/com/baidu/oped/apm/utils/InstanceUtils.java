package com.baidu.oped.apm.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.mvc.vo.InstanceVo;

/**
 * Created by mason on 8/28/15.
 */
public class InstanceUtils {
    public static List<InstanceVo> toInstanceVo(Application application, Iterable<Instance> appInstances,
                                                Iterable<InstanceStatistic> instanceStatistics) {
        Assert.notNull(application);
        Assert.notNull(appInstances);
        Assert.notNull(instanceStatistics);
        List<InstanceVo> instances = StreamSupport.stream(appInstances.spliterator(), false)
                                             .map(instance -> {
                                                 InstanceVo instanceVo = new InstanceVo();
                                                 instanceVo.setInstanceId(instance.getId());
                                                 instanceVo.setInstanceName(buildName(instance, application));
                                                 return instanceVo;
                                             }).collect(Collectors.toList());
        return instances;
    }

    private static String buildName(Instance instance, Application application) {
        Assert.notNull(instance, "Instance must not be null while building instance name.");
        Assert.notNull(application, "Application must not be null while building instance name.");

        return String.format("%s:%s:%s(%s)", application.getAppType(), instance.getHost(), instance.getPort(),
                                    application.getAppName());
    }
}
