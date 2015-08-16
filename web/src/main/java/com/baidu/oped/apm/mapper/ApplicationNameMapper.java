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


import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.mvc.vo.Application;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 */
@Component("applicationNameMapper")
public class ApplicationNameMapper implements RowMapper<List<Application>> {
    @Override
    public List<Application> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }

    //    @Override
//    public List<Application> mapRow(Result result, int rowNum) throws Exception {
//        if (result.isEmpty()) {
//            return Collections.emptyList();
//        }
//        Set<Short> uniqueTypeCodes = new HashSet<Short>();
//        String applicationName = Bytes.toString(result.getRow());
//
//        List<Cell> list = result.listCells();
//        if (list == null) {
//            return Collections.emptyList();
//        }
//
//        for (Cell cell : list) {
//            short serviceTypeCode = Bytes.toShort(CellUtil.cloneValue(cell));
//            uniqueTypeCodes.add(serviceTypeCode);
//        }
//        List<Application> applications = new ArrayList<Application>();
//        for (short serviceTypeCode : uniqueTypeCodes) {
//            applications.add(new Application(applicationName, ServiceType.findServiceType(serviceTypeCode)));
//        }
//
//        return applications;
//    }
}
