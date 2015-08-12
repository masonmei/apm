
package com.baidu.oped.apm.rpc.client;

/**
 * class PinpointSocketReconnectEventListener 
 *
 * @author meidongxu@baidu.com
 */
public interface PinpointSocketReconnectEventListener {

    /*
        there is no event except "reconnect" currently.
        when additional events are needed, it will be useful to pass with Event
    */
    void reconnectPerformed(PinpointSocket socket);

}
