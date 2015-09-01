package com.baidu.oped.apm.statistics.collector;

import com.baidu.oped.apm.statistics.collector.record.reader.BaseReader;

/**
 * Created by mason on 8/29/15.
 */
public abstract class BaseCollector {
    private BaseReader reader;



    public void collect(){
        Iterable iterable = reader.readItems();
    }
}
