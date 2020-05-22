package org.visallo.core.logging;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RequestExternalMetadata {
    public static LogMessage.MDCWriter of(ExternalMetadata externalMetadata) {
        return LogMessage.MDCWriter.Composite.of(
                new LogMessage.MDCWriter.KeyValue(IdConverter.key, externalMetadata.getId()),
                new LogMessage.MDCWriter.KeyValue(OperationConverter.key, externalMetadata.getOperation())
        );
    }

    public static class IdConverter extends ClassicConverter {
        public static final String key = "requestExternalId";

        //region ClassicConverter Implementation
        @Override
        public String convert(ILoggingEvent iLoggingEvent) {
            return iLoggingEvent.getMDCPropertyMap().getOrDefault(key, "");
        }
        //endregion
    }

    public static class OperationConverter extends ClassicConverter {
        public static final String key = "requestExternalOperation";

        //region ClassicConverter Implementation
        @Override
        public String convert(ILoggingEvent iLoggingEvent) {
            return iLoggingEvent.getMDCPropertyMap().getOrDefault(key, "");
        }
        //endregion
    }
}
