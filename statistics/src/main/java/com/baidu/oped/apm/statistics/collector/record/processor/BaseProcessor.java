package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ClearableAgentInfo;
import com.baidu.oped.apm.common.jpa.repository.AgentInstanceMapRepository;
import com.baidu.oped.apm.statistics.exceptions.NeedRunLaterException;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseProcessor<F extends ClearableAgentInfo, T> {
    @Autowired
    private AgentInstanceMapRepository agentInstanceMapRepository;

    public abstract Iterable<T> process(Iterable<F> items);

    public void preProcess(Iterable<F> items) {
        Set<Long> agentIds = StreamSupport.stream(items.spliterator(), false).map(ClearableAgentInfo::getAgentId)
                .collect(Collectors.toSet());
        List<AgentInstanceMap> agents = agentInstanceMapRepository.findAll(agentIds);
        agents.forEach(map -> {
            if (map.getInstanceId() == null) {
                throw new NeedRunLaterException("some agentInstanceMaps do not have instance Id.");
            }
        });
    }

    public Map<Long, AgentInstanceMap> getAgentInstanceMaps(Iterable<F> items) {
        Set<Long> agentIds = StreamSupport.stream(items.spliterator(), false).map(ClearableAgentInfo::getAgentId)
                .collect(Collectors.toSet());
        List<AgentInstanceMap> agents = agentInstanceMapRepository.findAll(agentIds);
        Map<Long, AgentInstanceMap> result = new HashMap<>();
        agents.forEach(map -> result.put(map.getId(), map));
        return result;
    }

    public void postProcess() {

    }
}
