package org.haifan.merlin.service;

/**
 * TODO: add javadoc
 * @param <T>
 */
public interface StreamHandler<T> {
    void onNext(T chunk);

    void onError(Throwable throwable);

    void onComplete();
}
