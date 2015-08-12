
package com.baidu.oped.apm.profiler.modifier.tomcat;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modifier to collect Tomcat connector information
 *
 * @author netspider
 */
public class TomcatConnectorModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TomcatConnectorModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "org/apache/catalina/connector/Connector";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }
        
        try {
            // Get protocol and port by intercepting initialize()
            Interceptor interceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain,
                    "com.baidu.oped.apm.profiler.modifier.tomcat.interceptor.ConnectorInitializeInterceptor", null, null);
            InstrumentClass connector = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            // Tomcat 6
            if (connector.hasDeclaredMethod("initialize", null)) {
                connector.addInterceptor("initialize", null, interceptor);
            }
            // Tomcat 7
            else if (connector.hasDeclaredMethod("initInternal", null)) {
                connector.addInterceptor("initInternal", null, interceptor);
            }

            if (this.logger.isInfoEnabled()) {
                this.logger.info("{} class is converted.", javassistClassName);
            }

            return connector.toBytecode();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
            return null;
        }
    }
}