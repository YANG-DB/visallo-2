package org.visallo.core.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import static org.visallo.core.logging.LogUtils.MDC_CLIENT_IP_ADDRESS;
import static org.visallo.core.logging.LogUtils.MDC_USER_ID;

public class ClientIP {
    public static LogMessage.MDCWriter of(String requestId) {
        return new LogMessage.MDCWriter.KeyValue(Converter.key, requestId);
    }

    public static class Converter extends ClassicConverter {
        public static final String key = MDC_CLIENT_IP_ADDRESS;

        //region ClassicConverter Implementation
        @Override
        public String convert(ILoggingEvent iLoggingEvent) {
            return iLoggingEvent.getMDCPropertyMap().getOrDefault(key, "");
        }
        //endregion
    }
}
