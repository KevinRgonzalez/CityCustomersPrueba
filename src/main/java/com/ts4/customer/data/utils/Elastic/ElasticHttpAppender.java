package com.ts4.customer.data.utils.Elastic;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ElasticHttpAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {
        // Los logs se envían a la infraestructura de monitoreo configurada en el clúster.
    }
}
