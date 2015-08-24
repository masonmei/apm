package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_service_info database table.
 * 
 */
@Entity
@Table(name="apm_service_info")
public class ServiceInfo extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="service_libs", nullable=false, length=1024)
	private String serviceLibs;

	@Column(name="service_name", nullable=false, length=255)
	private String serviceName;

	//bi-directional many-to-one association to ServerMetaData
	@ManyToOne
	@JoinColumn(name="server_meta_data_id", nullable=false)
	private ServerMetaData serverMetaData;

	public ServiceInfo() {
	}

	public String getServiceLibs() {
		return this.serviceLibs;
	}

	public void setServiceLibs(String serviceLibs) {
		this.serviceLibs = serviceLibs;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public ServerMetaData getServerMetaData() {
		return this.serverMetaData;
	}

	public void setServerMetaData(ServerMetaData serverMetaData) {
		this.serverMetaData = serverMetaData;
	}

}