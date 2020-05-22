package org.visallo.core.model.longRunningProcess;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.google.inject.Inject;
import org.json.JSONObject;
import org.visallo.core.logging.MethodName;
import org.visallo.core.status.MetricsManager;
import org.visallo.core.util.VisalloLogger;
import org.visallo.core.util.VisalloLoggerFactory;

import static org.visallo.core.logging.LogUtils.call;

public abstract class LongRunningProcessWorker<T> {
    private static final VisalloLogger LOGGER = VisalloLoggerFactory.getLogger(LongRunningProcessWorker.class);
    private MetricsManager metricsManager;
    private Counter totalProcessedCounter;
    private Counter totalErrorCounter;
    private Counter processingCounter;
    private Timer processingTimeTimer;

    public void prepare(LongRunningWorkerPrepareData workerPrepareData) {
        totalProcessedCounter = getMetricsManager().counter(this, "total-processed");
        processingCounter = getMetricsManager().counter(this, "processing");
        totalErrorCounter = getMetricsManager().counter(this, "total-errors");
        processingTimeTimer = getMetricsManager().timer(this, "processing-time");
    }

    public abstract boolean isHandled(JSONObject longRunningProcessQueueItem);

    protected abstract VisalloLogger getLogger();

    public final void process(JSONObject longRunningProcessQueueItem) {
        try (Timer.Context t = processingTimeTimer.time()) {
            processingCounter.inc();
            try {
                call(getLogger().logger, MethodName.of(getClass().getSimpleName()), () -> processInternal(longRunningProcessQueueItem));
                processInternal(longRunningProcessQueueItem);
            } finally {
                processingCounter.dec();
            }
            totalProcessedCounter.inc();
        } catch (Throwable ex) {
            LOGGER.error("Failed to complete long running process: " + longRunningProcessQueueItem, ex);
            this.totalErrorCounter.inc();
            throw ex;
        }
    }

    protected abstract T processInternal(JSONObject longRunningProcessQueueItem);

    @Inject
    public final void setMetricsManager(MetricsManager metricsManager) {
        this.metricsManager = metricsManager;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }
}
