package com.baidu.oped.apm.utils;

import com.baidu.oped.apm.common.jpa.entity.Instance;
import org.springframework.util.Assert;

/**
 * Created by mason on 10/12/15.
 */
public abstract class NameUtils {
    public static String buildInstanceName(Instance instance) {
        Assert.notNull(instance, "Cannot build instance name with a not exist instance");
        StringBuilder builder = new StringBuilder();
        builder.append(instance.getHost());
        if (instance.getPort() != null) {
            builder.append(":").append(instance.getPort());
        }
        return builder.toString();
    }
}
