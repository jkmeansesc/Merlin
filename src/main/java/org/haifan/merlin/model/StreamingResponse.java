package org.haifan.merlin.model;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;

import java.util.function.Consumer;

/**
 * Represents a streaming response that emits items of type {@code T}.
 *
 * @param <T> the type of the emitted items
 */
@SuppressWarnings("unused")
public class StreamingResponse<T> {
    private final Flowable<T> flowable;
    private Disposable disposable;

    /**
     * Constructs a StreamingResponse with the given Flowable.
     *
     * @param flowable the Flowable that emits items of type {@code T}
     */
    public StreamingResponse(Flowable<T> flowable) {
        this.flowable = flowable;
    }

    /**
     * Starts the streaming response and handles the emitted items, errors, and completion.
     *
     * @param onNext     the consumer to handle each emitted item
     * @param onError    the consumer to handle errors
     * @param onComplete the action to execute on completion
     */
    public void start(Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete) {
        disposable = flowable.subscribe(
                onNext::accept,
                onError::accept,
                onComplete
        );
    }

    /**
     * Cancels the streaming response if it is active.
     */
    public void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * Returns the Flowable that represents the streaming response.
     *
     * @return the Flowable emitting items of type {@code T}
     */
    public Flowable<T> asFlowable() {
        return flowable;
    }
}
