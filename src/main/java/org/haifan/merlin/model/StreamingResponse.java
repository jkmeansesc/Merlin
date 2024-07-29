package org.haifan.merlin.model;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;

import java.util.function.Consumer;

/**
 * TODO: add javadoc
 *
 * @param <T>
 */
public class StreamingResponse<T> {
    private final Flowable<T> flowable;
    private Disposable disposable;

    public StreamingResponse(Flowable<T> flowable) {
        this.flowable = flowable;
    }

    public void start(Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete) {
        disposable = flowable.subscribe(
                onNext::accept,
                onError::accept,
                onComplete
        );
    }

    public void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public Flowable<T> asFlowable() {
        return flowable;
    }
}
