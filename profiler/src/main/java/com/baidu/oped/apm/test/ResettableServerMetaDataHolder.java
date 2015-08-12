
package com.baidu.oped.apm.test;

import java.util.List;

import com.baidu.oped.apm.profiler.context.DefaultServerMetaDataHolder;

/**
 * class ResettableServerMetaDataHolder 
 *
 * @author meidongxu@baidu.com
 */



public class ResettableServerMetaDataHolder extends DefaultServerMetaDataHolder {

    public ResettableServerMetaDataHolder(List<String> vmArgs) {
        super(vmArgs);
    }

    public void reset() {
        this.serverName = null;
        this.serviceInfos.clear();
    }

}
