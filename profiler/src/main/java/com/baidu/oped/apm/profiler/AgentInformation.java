
package com.baidu.oped.apm.profiler;

import com.baidu.oped.apm.common.ServiceType;

import java.util.HashMap;
import java.util.Map;

/**
 * class AgentInformation 
 *
 * @author meidongxu@baidu.com
 */
public class AgentInformation {
    private final String agentId;
    private final String applicationName;
    private final long startTime;
    private final int pid;
    private final String machineName;
    private final String hostIp;
    private final short serverType;
    private final ServiceType serverServiceType;
    private final String version;

    public AgentInformation(String agentId, String applicationName, long startTime, int pid, String machineName, String hostIp, short serverType, String version) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (applicationName == null) {
            throw new NullPointerException("applicationName must not be null");
        }
        if (machineName == null) {
            throw new NullPointerException("machineName must not be null");
        }
        if (version == null) {
            throw new NullPointerException("version must not be null");
        }
        this.agentId = agentId;
        this.applicationName = applicationName;
        this.startTime = startTime;
        this.pid = pid;
        this.machineName = machineName;
        this.hostIp = hostIp;
        this.serverType = serverType;
        this.serverServiceType =  ServiceType.findServiceType(serverType);
        this.version = version;
    }



    public String getAgentId() {
        return agentId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getPid() {
        return pid;
    }

    public String getMachineName() {
        return machineName;
    }
    
    public String getHostIp() {
        return hostIp;
    }

    public short getServerType() {
        return serverType;
    }

    public ServiceType getServerServiceType() {
        return serverServiceType;
    }

    public String getVersion() {
        return version;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(AgentHandshakePropertyType.AGENT_ID.getName(), this.agentId);
        map.put(AgentHandshakePropertyType.APPLICATION_NAME.getName(), this.applicationName);
        map.put(AgentHandshakePropertyType.HOSTNAME.getName(), this.machineName);
        map.put(AgentHandshakePropertyType.IP.getName(), this.hostIp);
        map.put(AgentHandshakePropertyType.PID.getName(), this.pid);
        map.put(AgentHandshakePropertyType.SERVICE_TYPE.getName(), this.serverType);
        map.put(AgentHandshakePropertyType.START_TIMESTAMP.getName(), this.startTime);
        map.put(AgentHandshakePropertyType.VERSION.getName(), this.version);

        return map;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AgentInformation{");
        sb.append("agentId='").append(agentId).append('\'');
        sb.append(", applicationName='").append(applicationName).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", pid=").append(pid);
        sb.append(", machineName='").append(machineName).append('\'');
        sb.append(", hostIp='").append(hostIp).append('\'');
        sb.append(", serverType=").append(serverType);
        sb.append(", version='").append(version).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
