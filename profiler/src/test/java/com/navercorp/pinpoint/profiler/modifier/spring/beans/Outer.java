
package com.baidu.oped.apm.profiler.modifier.spring.beans;

/**
 * class Outer 
 *
 * @author meidongxu@baidu.com
 */
public class Outer {
    private Inner inner;
    
    public void setInner(Inner inner) {
        this.inner = inner;
    }
    
    public Inner getInner() {
        return inner;
    }

    public void doSomething() {
        
    }
}
