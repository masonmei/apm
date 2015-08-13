/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.oped.apm.mapper;



import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

import com.baidu.oped.apm.common.bo.AnnotationBo;
import com.baidu.oped.apm.common.bo.SpanBo;
import com.baidu.oped.apm.common.bo.SpanEventBo;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.mvc.vo.TransactionId;

/**
 * @author emeroad
 */
@Component
public class SpanMapper implements RowMapper<List<SpanBo>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AnnotationMapper annotationMapper;

    @Override
    public List<SpanBo> mapRow(Result result, int rowNum) throws Exception {
        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        byte[] rowKey = result.getRow();
        final TransactionId transactionId = new TransactionId(rowKey, TransactionId.DISTRIBUTE_HASH_SIZE);

        final Cell[] rawCells = result.rawCells();
        List<SpanBo> spanList = new ArrayList<SpanBo>();
        Map<Long, SpanBo> spanMap = new HashMap<Long, SpanBo>();
        List<SpanEventBo> spanEventBoList = new ArrayList<SpanEventBo>();
        for (Cell cell : rawCells) {
            // only if family name is "span"
            if (CellUtil.matchingFamily(cell, HBaseTables.TRACES_CF_SPAN)) {

                SpanBo spanBo = new SpanBo();
                spanBo.setTraceAgentId(transactionId.getAgentId());
                spanBo.setTraceAgentStartTime(transactionId.getAgentStartTime());
                spanBo.setTraceTransactionSequence(transactionId.getTransactionSequence());
                spanBo.setCollectorAcceptTime(cell.getTimestamp());

                spanBo.setSpanID(Bytes.toLong(cell.getQualifierArray(), cell.getQualifierOffset()));
                spanBo.readValue(cell.getValueArray(), cell.getValueOffset());
                if (logger.isDebugEnabled()) {
                    logger.debug("read span :{}", spanBo);
                }
                spanList.add(spanBo);
                spanMap.put(spanBo.getSpanId(), spanBo);
            } else if (CellUtil.matchingFamily(cell, HBaseTables.TRACES_CF_TERMINALSPAN)) {
                SpanEventBo spanEventBo = new SpanEventBo();
                spanEventBo.setTraceAgentId(transactionId.getAgentId());
                spanEventBo.setTraceAgentStartTime(transactionId.getAgentStartTime());
                spanEventBo.setTraceTransactionSequence(transactionId.getTransactionSequence());

                // qualifier : spanId(long) + sequence(short) + asyncId(int)
                long spanId = Bytes.toLong(cell.getQualifierArray(), cell.getQualifierOffset());

                // because above spanId type is "long", so offset is 8
                final int spanIdOffset = 8;
                short sequence = Bytes.toShort(cell.getQualifierArray(), cell.getQualifierOffset() + spanIdOffset);
                spanEventBo.setSpanId(spanId);
                spanEventBo.setSequence(sequence);

                spanEventBo.readValue(cell.getValueArray(), cell.getValueOffset());
                if (logger.isDebugEnabled()) {
                    logger.debug("read spanEvent :{}", spanEventBo);
                }
                spanEventBoList.add(spanEventBo);
            }
        }
        for (SpanEventBo spanEventBo : spanEventBoList) {
            SpanBo spanBo = spanMap.get(spanEventBo.getSpanId());
            if (spanBo != null) {
                spanBo.addSpanEvent(spanEventBo);
            }
        }
        if (annotationMapper != null) {
            Map<Long, List<AnnotationBo>> annotationMap = annotationMapper.mapRow(result, rowNum);
            addAnnotation(spanList, annotationMap);
        }


        return spanList;

    }

    private void addAnnotation(List<SpanBo> spanList, Map<Long, List<AnnotationBo>> annotationMap) {
        for (SpanBo bo : spanList) {
            long spanID = bo.getSpanId();
            List<AnnotationBo> anoList = annotationMap.get(spanID);
            bo.setAnnotationBoList(anoList);
        }
    }
}
