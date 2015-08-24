package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.HostApplicationMapDao;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;
import com.baidu.oped.apm.collector.util.AtomicLongUpdateMap;
import com.baidu.oped.apm.common.jpa.entity.HostApplicationMap;
import com.baidu.oped.apm.common.jpa.repository.HostApplicationMapRepository;
import com.baidu.oped.apm.common.util.TimeSlot;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcHostApplicationMapDao implements HostApplicationMapDao {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcHostApplicationMapDao.class);
    private final AtomicLongUpdateMap<CacheKey> updater = new AtomicLongUpdateMap<CacheKey>();

    @Autowired
    private HostApplicationMapRepository hostApplicationMapRepository;

    @Autowired
    private AcceptedTimeService acceptedTimeService;

    @Autowired
    private TimeSlot timeSlot;

    @Override
    public void insert(String host, String bindApplicationName, short bindServiceType, String parentApplicationName,
                       short parentServiceType) {
        if (host == null) {
            throw new NullPointerException("host must not be null");
        }
        if (bindApplicationName == null) {
            throw new NullPointerException("bindApplicationName must not be null");
        }

        final long statisticsRowSlot = getSlotTime();

        final CacheKey cacheKey =
                new CacheKey(host, bindApplicationName, bindServiceType, parentApplicationName, parentServiceType);
        final boolean needUpdate = updater.update(cacheKey, statisticsRowSlot);
        if (needUpdate) {
            insertHostVer2(host, bindApplicationName, bindServiceType, statisticsRowSlot, parentApplicationName,
                                  parentServiceType);
        }
    }

    private long getSlotTime() {
        final long acceptedTime = acceptedTimeService.getAcceptedTime();
        return timeSlot.getTimeSlot(acceptedTime);
    }

    private void insertHostVer2(String host, String bindApplicationName, short bindServiceType, long statisticsRowSlot,
                                String parentApplicationName, short parentServiceType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Insert host-application map. host={}, bindApplicationName={}, bindServiceType={}, "
                                 + "parentApplicationName={}, parentServiceType={}", host, bindApplicationName,
                                bindServiceType, parentApplicationName, parentServiceType);
        }

        // TODO should consider to add bellow codes again later.
        //String parentAgentId = null;
        //final byte[] rowKey = createRowKey(parentApplicationName, parentServiceType, statisticsRowSlot,
        // parentAgentId);
        HostApplicationMap applicationMap = new HostApplicationMap();
        applicationMap.setParentAgentId(null);
        applicationMap.setParentApplicationName(parentApplicationName);
        applicationMap.setParentServiceType(parentServiceType);
        applicationMap.setStatisticsRowSlot(statisticsRowSlot);

        applicationMap.setHost(host);
        applicationMap.setBindApplicationName(bindApplicationName);
        applicationMap.setBindServiceType(bindServiceType);

        try {
            hostApplicationMapRepository.save(applicationMap);
        } catch (Exception ex) {
            LOG.warn("retry one. Caused:{}", ex.getCause(), ex);
        }
    }

    private static final class CacheKey {
        private final String host;
        private final String applicationName;
        private final short serviceType;

        private final String parentApplicationName;
        private final short parentServiceType;

        public CacheKey(String host, String applicationName, short serviceType, String parentApplicationName,
                        short parentServiceType) {
            if (host == null) {
                throw new NullPointerException("host must not be null");
            }
            if (applicationName == null) {
                throw new NullPointerException("bindApplicationName must not be null");
            }
            this.host = host;
            this.applicationName = applicationName;
            this.serviceType = serviceType;

            // may be null for below two parent values.
            this.parentApplicationName = parentApplicationName;
            this.parentServiceType = parentServiceType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CacheKey cacheKey = (CacheKey) o;

            if (parentServiceType != cacheKey.parentServiceType) {
                return false;
            }
            if (serviceType != cacheKey.serviceType) {
                return false;
            }
            if (!applicationName.equals(cacheKey.applicationName)) {
                return false;
            }
            if (!host.equals(cacheKey.host)) {
                return false;
            }
            if (parentApplicationName != null ? !parentApplicationName.equals(cacheKey.parentApplicationName)
                        : cacheKey.parentApplicationName != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = host.hashCode();
            result = 31 * result + applicationName.hashCode();
            result = 31 * result + (int) serviceType;
            result = 31 * result + (parentApplicationName != null ? parentApplicationName.hashCode() : 0);
            result = 31 * result + (int) parentServiceType;
            return result;
        }
    }

}
