
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.jpa.entity.Trace;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * class BusinessTransactions 
 *
 * @author meidongxu@baidu.com
 */
public class BusinessTransactions {

    private final Map<String, BusinessTransaction> transactions = new HashMap<String, BusinessTransaction>();

    private int totalCallCount;

    public void add(Trace span) {
        this.add(span, "rpc");
    }

    public void add(Trace span, String fieldName) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        totalCallCount++;
        try {
            Field field = Trace.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            String key = field.get(span).toString();
            if (transactions.containsKey(key)) {
                transactions.get(key).add(span);
            } else {
                transactions.put(key, new BusinessTransaction(span));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<BusinessTransaction> getBusinessTransaction() {
        return transactions.values();
    }

    public int getTotalCallCount() {
        return totalCallCount;
    }

    public int getURLCount() {
        return transactions.size();
    }

    public Map<String, BusinessTransaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "BusinessTransactions{" +
                "transactions=" + transactions +
                ", totalCallCount=" + totalCallCount +
                '}';
    }
}
