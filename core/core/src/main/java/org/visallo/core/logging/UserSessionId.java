package org.visallo.core.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class UserSessionId {
    public static LogMessage.MDCWriter of(String requestId) {
        return new LogMessage.MDCWriter.KeyValue(Converter.key, requestId);
    }

    public static class Converter extends ClassicConverter {
        public static final String key = "userSessionId";

        //region ClassicConverter Implementation
        @Override
        public String convert(ILoggingEvent iLoggingEvent) {
            return iLoggingEvent.getMDCPropertyMap().getOrDefault(key, "");
        }
        //endregion
    }
}
