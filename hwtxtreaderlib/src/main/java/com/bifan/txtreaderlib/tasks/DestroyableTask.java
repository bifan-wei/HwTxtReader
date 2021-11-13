package com.bifan.txtreaderlib.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: bifan-wei
 * 2021/11/13.
 */
public class DestroyableTask {
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private final IShutdownCall mShutdownCall;

    public DestroyableTask(IShutdownCall shutdownCall) {
        this.mShutdownCall = shutdownCall;
    }

    public void excuse(Runnable runnable) {
        mExecutorService.submit(runnable);
    }

    public void destroy() {
        if (mShutdownCall != null) {
            mShutdownCall.clearIfNeedBeforeShutdown();
        }
        mExecutorService.shutdownNow();
    }

    public interface IShutdownCall {
        void clearIfNeedBeforeShutdown();
    }
}
