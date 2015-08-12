
package com.baidu.oped.apm.rpc;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * class DiscardPipelineFactory 
 *
 * @author meidongxu@baidu.com
 */
public class DiscardPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        return Channels.pipeline(new DiscardServerHandler());
    }
}
