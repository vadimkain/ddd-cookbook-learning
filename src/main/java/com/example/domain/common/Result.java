package com.example.domain.common;

import java.io.Serializable;

@SuppressWarnings({"unchecked", "RedundantSuppression"})
public final class Result<T> implements Serializable {
    private final Object value;

    private Result(Object value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return !(value instanceof Failure);
    }

    public boolean isFailure() {
        return value instanceof Failure;
    }

    public T getOrNull() {
        if (isFailure()) return null;
        else return (T) value;
    }

    public Throwable exceptionOrNull() {
        if (value instanceof Failure) {
            return ((Failure) value).exception;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (value instanceof Failure) {
            return ((Failure) value).toString();
        } else {
            return "Success(" + value + ")";
        }
    }

    public static final class Failure implements Serializable {
        private final Throwable exception;

        private Failure(Throwable exception) {
            this.exception = exception;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Failure failure = (Failure) o;

            return exception.equals(failure.exception);
        }

        @Override
        public int hashCode() {
            return exception.hashCode();
        }

        @Override
        public String toString() {
            return "Failure(" + exception + ")";
        }
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    public static <T> Result<T> failure(Throwable exception) {
        return new Result<>(createFailure(exception));
    }

    private static Object createFailure(Throwable exception) {
        return new Failure(exception);
    }

    private void throwOnFailure() throws Throwable {
        if (value instanceof Failure) {
            throw ((Failure) value).exception;
        }
    }

    public T getOrThrow() throws Throwable {
        throwOnFailure();
        return (T) value;
    }

    public <R> R getOrElse(java.util.function.Function<? super Throwable, ? extends R> onFailure) {
        if (value instanceof Failure) {
            return onFailure.apply(((Failure) value).exception);
        } else {
            return (R) value;
        }
    }

    public <R> R getOrDefault(R defaultValue) {
        if (isFailure()) {
            return defaultValue;
        } else {
            return (R) value;
        }
    }

    public <R> R fold(java.util.function.Function<? super T, ? extends R> onSuccess, java.util.function.Function<? super Throwable, ? extends R> onFailure) {
        if (isSuccess()) {
            return onSuccess.apply((T) value);
        } else {
            return onFailure.apply(((Failure) value).exception);
        }
    }

    public <R> Result<R> map(java.util.function.Function<? super T, ? extends R> transform) {
        if (isSuccess()) {
            return success(transform.apply((T) value));
        } else {
            return (Result<R>) this;
        }
    }

    public <R> Result<R> mapCatching(java.util.function.Function<? super T, ? extends R> transform) {
        if (isSuccess()) {
            try {
                return success(transform.apply((T) value));
            } catch (Throwable t) {
                return failure(t);
            }
        } else {
            return (Result<R>) this;
        }
    }

    public <R> Result<R> recover(java.util.function.Function<? super Throwable, ? extends R> transform) {
        if (isFailure()) {
            return success(transform.apply(((Failure) value).exception));
        } else {
            return (Result<R>) this;
        }
    }

    public <R> Result<R> recoverCatching(java.util.function.Function<? super Throwable, ? extends R> transform) {
        if (isFailure()) {
            try {
                return success(transform.apply(((Failure) value).exception));
            } catch (Throwable t) {
                return failure(t);
            }
        } else {
            return (Result<R>) this;
        }
    }

    public Result<T> onFailure(java.util.function.Consumer<? super Throwable> action) {
        if (isFailure()) {
            action.accept(((Failure) value).exception);
        }
        return this;
    }

    public Result<T> onSuccess(java.util.function.Consumer<? super T> action) {
        if (isSuccess()) {
            action.accept((T) value);
        }
        return this;
    }
}
