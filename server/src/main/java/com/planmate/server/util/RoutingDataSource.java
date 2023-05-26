package com.planmate.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    /**
     * Transaction이 read-only면 slave
     * */
    @Override
    protected Object determineCurrentLookupKey() {
        log.info(TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master");

        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master";
    }
}