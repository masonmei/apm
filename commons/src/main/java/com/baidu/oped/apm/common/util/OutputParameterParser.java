
package com.baidu.oped.apm.common.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * class OutputParameterParser 
 *
 * @author meidongxu@baidu.com
 */
public class OutputParameterParser {

    public static final char SEPARATOR = DefaultParsingResult.SEPARATOR;

    public List<String> parseOutputParameter(String outputParams) {
        // may also need to know about the parsing result 
        if (outputParams == null || outputParams.length() == 0) {
            return Collections.emptyList();
        }

        final List<String> result = new LinkedList<String>();
        StringBuilder params = new StringBuilder();
        for (int index = 0; index < outputParams.length(); index++) {
            final char ch = outputParams.charAt(index);
            if (ch == SEPARATOR) {
                if (lookAhead1(outputParams, index) == SEPARATOR) {
                    params.append(SEPARATOR);
                    index++;
                } else {
                    result.add(params.toString());
                    params = new StringBuilder();
                }
            } else {
                params.append(ch);
            }
        }

        result.add(params.toString());

        return result;
    }

    private int lookAhead1(String sql, int index) {
        index++;
        if (index < sql.length()) {
            return sql.charAt(index);
        } else {
            return -1;
        }
    }

}
