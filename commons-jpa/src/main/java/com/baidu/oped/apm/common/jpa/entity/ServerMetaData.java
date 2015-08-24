package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_server_meta_data database table.
 * 
 */
@Entity
@Table(name="apm_server_meta_data")
public class ServerMetaData extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="vm_args", nullable=false, length=1024)
	private String vmArgs;

	//bi-directional many-to-one association to ServiceInfo
	@OneToMany(mappedBy="serverMetaData", cascade = CascadeType.ALL)
	private List<ServiceInfo> serviceInfos = new ArrayList<>();

	@OneToOne(mappedBy = "serverMetaData")
	private AgentInfo agentInfo;

	public ServerMetaData() {
	}

	public String getVmArgs() {
		return this.vmArgs;
	}

	public void setVmArgs(String vmArgs) {
		this.vmArgs = vmArgs;
	}

	public List<ServiceInfo> getServiceInfos() {
		return this.serviceInfos;
	}

	public void setServiceInfos(List<ServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}

	public ServiceInfo addServiceInfo(ServiceInfo serviceInfo) {
		getServiceInfos().add(serviceInfo);
		serviceInfo.setServerMetaData(this);

		return serviceInfo;
	}

	public ServiceInfo removeServiceInfo(ServiceInfo serviceInfo) {
		getServiceInfos().remove(serviceInfo);
		serviceInfo.setServerMetaData(null);

		return serviceInfo;
	}

	public AgentInfo getAgentInfo() {
		return agentInfo;
	}

	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
}