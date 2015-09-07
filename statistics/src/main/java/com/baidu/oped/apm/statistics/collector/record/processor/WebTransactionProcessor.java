package com.baidu.oped.apm.statistics.collector.record.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.QWebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/1/15.
 */
@Component
public class WebTransactionProcessor extends BaseTraceProcessor<WebTransactionStatistic> {

    @Autowired
    private WebTransactionRepository webTransactionRepository;

    public void setWebTransactionRepository(WebTransactionRepository webTransactionRepository) {
        this.webTransactionRepository = webTransactionRepository;
    }

    @Override
    public WebTransactionStatistic findOrCreateStatisticInstance(WebTransactionGroup mappedKey) {
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(mappedKey.getAppId());
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(mappedKey.getInstanceId());
        BooleanExpression rpcCondition = qWebTransaction.rpc.eq(mappedKey.getRpc());
        BooleanExpression whereCondition = appIdCondition.and(instanceIdCondition).and(rpcCondition);

        WebTransaction one = webTransactionRepository.findOne(whereCondition);
        if (one == null) {
            WebTransaction webTransaction = new WebTransaction();
            webTransaction.setAppId(mappedKey.getAppId());
            webTransaction.setInstanceId(mappedKey.getInstanceId());
            webTransaction.setRpc(mappedKey.getRpc());

            try {
                one = webTransactionRepository.save(webTransaction);
            } catch (DataAccessException exception) {
                one = webTransactionRepository.findOne(whereCondition);
            }
        }
        WebTransactionStatistic webTransactionStatistic = new WebTransactionStatistic();
        webTransactionStatistic.setTransactionId(one.getId());
        return webTransactionStatistic;
    }
}
