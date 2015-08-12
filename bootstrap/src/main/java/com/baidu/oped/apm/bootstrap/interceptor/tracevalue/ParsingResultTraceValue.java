package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

import com.baidu.oped.apm.common.util.ParsingResult;

/**
 * class ParsingResultTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface ParsingResultTraceValue extends TraceValue {
    void __setTraceParsingResult(ParsingResult parsingResult);

    ParsingResult __getTraceParsingResult();
}
