package org.visallo.core.logging;

import org.slf4j.MDC;
import org.visallo.core.status.JmxMetricsManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

import static org.visallo.core.logging.LogMessage.Level.*;


public class LogUtils {

    //region Protected Methods
    public static LogMessage.MDCWriter primerMdcWriter(String requestId, ExternalMetadata externalId) {
        return LogMessage.MDCWriter.Composite.of(
                Elapsed.now(),
                RequestId.of(requestId),
                RequestExternalMetadata.of(externalId));
    }

    public static <TResult> TResult call(org.slf4j.Logger logger, MethodName.MDCWriter methodName, Supplier<TResult> methodInvocationSupplier) {
        return new LoggingSyncMethodDecorator<TResult>(
                logger,
                JmxMetricsManager.REGISTRY,
                methodName,
                primerMdcWriter(MDC.get(RequestId.Converter.key),
                        new ExternalMetadata(MDC.get(UserSessionId.Converter.key), MDC.get(MethodName.Converter.key))),
                Collections.singletonList(trace),
                Arrays.asList(info, trace))
                .decorate(methodInvocationSupplier, new MethodDecorator.ResultHandler.Standard<>());

    }
}
