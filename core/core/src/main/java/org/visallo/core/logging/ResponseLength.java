package org.visallo.core.logging;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ResponseLength {
    public static LogMessage.MDCWriter of(int length) {
        return new LogMessage.MDCWriter.KeyValue(Converter.key, Integer.toString(length));
    }

    public static class Converter extends ClassicConverter {
        public static final String key = "responseLength";

        //region ClassicConverter Implementation
        @Override
        public String convert(ILoggingEvent iLoggingEvent) {
            return iLoggingEvent.getMDCPropertyMap().getOrDefault(key, "0");
        }
        //endregion
    }
}
