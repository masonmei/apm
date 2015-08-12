
package com.baidu.oped.apm.rpc.server;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.oped.apm.rpc.server.PinpointServerSocketState;
import com.baidu.oped.apm.rpc.server.PinpointServerSocketStateCode;

/**
 * class PinpointServerSocketStateTest 
 *
 * @author meidongxu@baidu.com
 */
public class PinpointServerSocketStateTest {

    // basic type of connection's lifecycle between peers.
    // RUN -> RUN_DUPLEX_COMMUNICATION ->  BEING_SHUTDOWN -> connection closed
    @Test
    public void changeStateTest1() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.RUN_DUPLEX);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_DUPLEX, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.SHUTDOWN, state.getCurrentState());
    }

    // basic type of connection's lifecycle between peers.
    // RUN_DUPLEX_COMMUNICATION -> RUN_DUPLEX_COMMUNICATION -> BEING_SHUTDOWN -> connection closed
    @Test
    public void changeStateTest2() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_DUPLEX);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_DUPLEX, state.getCurrentState());

        PinpointServerSocketStateCode currentState = state.changeState(PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertNull(currentState);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, state.getCurrentState());

        currentState = state.changeState(PinpointServerSocketStateCode.BEING_SHUTDOWN, PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, currentState);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.SHUTDOWN, state.getCurrentState());
    }

    @Test
    public void changeStateTest3() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.UNEXPECTED_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.UNEXPECTED_SHUTDOWN, state.getCurrentState());
    }

    @Test
    public void changeStateTest4() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_WITHOUT_HANDSHAKE, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.SHUTDOWN, state.getCurrentState());
    }

    @Test
    public void changeStateTest5() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_DUPLEX);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_DUPLEX, state.getCurrentState());

        state.changeState(PinpointServerSocketStateCode.SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.SHUTDOWN, state.getCurrentState());
    }

    @Test
    public void invalidChangeStateTest1() {
        PinpointServerSocketState state = new PinpointServerSocketState();
        PinpointServerSocketStateCode beforeCode = state.changeState(PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.NONE, beforeCode);
    }

    @Test(expected = IllegalStateException.class)
    public void invalidChangeStateTest2() {
        PinpointServerSocketState state = new PinpointServerSocketState();
        state.changeStateThrowWhenFailed(PinpointServerSocketStateCode.BEING_SHUTDOWN);
    }
    

    @Test
    public void invalidChangeStateTest3() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_DUPLEX);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_DUPLEX, state.getCurrentState());

        state.changeStateThrowWhenFailed(PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, state.getCurrentState());

        PinpointServerSocketStateCode beforeCode = state.changeState(PinpointServerSocketStateCode.UNEXPECTED_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, beforeCode);
    }

    @Test(expected = IllegalStateException.class)
    public void invalidChangeStateTest4() {
        PinpointServerSocketState state = new PinpointServerSocketState();

        state.changeState(PinpointServerSocketStateCode.RUN_DUPLEX);
        Assert.assertEquals(PinpointServerSocketStateCode.RUN_DUPLEX, state.getCurrentState());

        state.changeStateThrowWhenFailed(PinpointServerSocketStateCode.BEING_SHUTDOWN);
        Assert.assertEquals(PinpointServerSocketStateCode.BEING_SHUTDOWN, state.getCurrentState());

        PinpointServerSocketStateCode beforeCode = state.changeStateThrowWhenFailed(PinpointServerSocketStateCode.UNEXPECTED_SHUTDOWN);
    }

}
