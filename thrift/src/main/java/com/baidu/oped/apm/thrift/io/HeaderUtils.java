
package com.baidu.oped.apm.thrift.io;

/**
 * class HeaderUtils 
 *
 * @author meidongxu@baidu.com
 */
final class HeaderUtils {
    public static final int OK = Header.SIGNATURE;
    // TODO Maybe PASS_L4 should be a modifiable variable
    public static final int PASS_L4 = 85; // Udp
    public static final int FAIL = 0;

    private HeaderUtils() {
    }

    public static int validateSignature(byte signature) {
        if (Header.SIGNATURE == signature) {
            return OK;
        } else if (PASS_L4 == signature) {
            return PASS_L4;
        }
        return FAIL;
    }
}
