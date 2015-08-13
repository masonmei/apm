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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.bo.AnnotationBo;
import com.baidu.oped.apm.common.bo.AnnotationBoList;
import com.baidu.oped.apm.common.buffer.Buffer;
import com.baidu.oped.apm.common.buffer.OffsetFixedBuffer;
import com.baidu.oped.apm.common.hbase.HBaseTables;

/**
 * @author emeroad
 */
@Component
public class AnnotationMapper implements RowMapper<Map<Long, List<AnnotationBo>>> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<Long, List<AnnotationBo>> mapRow(Result result, int rowNum) throws Exception {
        if (result.isEmpty()) {
            return Collections.emptyMap();
        }
        Cell[] rawCells = result.rawCells();
        Map<Long, List<AnnotationBo>> annotationList = new HashMap<Long, List<AnnotationBo>>();

        for (Cell cell : rawCells) {

            Buffer buffer = new OffsetFixedBuffer(cell.getQualifierArray(), cell.getQualifierOffset());
            long spanId = buffer.readLong();

            if (CellUtil.matchingFamily(cell, HBaseTables.TRACES_CF_ANNOTATION)) {
                int valueLength = cell.getValueLength();
                if (valueLength == 0) {
                    continue;
                }

                buffer.setOffset(cell.getValueOffset());
                AnnotationBoList annotationBoList = new AnnotationBoList();
                annotationBoList.readValue(buffer);
                if (annotationBoList.size() > 0 ) {
                    annotationBoList.setSpanId(spanId);
                    annotationList.put(spanId, annotationBoList.getAnnotationBoList());
                }
            }
        }
        return annotationList;
    }
}
