package com.example.domain.common;

import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "RedundantSuppression"})
public final class Result<T> implements Serializable {
    private final T value;

    private Result(T value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return !(value instanceof Failure);
    }

    public boolean isFailure() {
        return value instanceof Failure;
    }

    public T getValue() {
        return (T) value;
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
        return (Result<T>) new Result<>(createFailure(exception));
    }

    private static Failure createFailure(Throwable exception) {
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

    public <R> R fold(Function<? super T, ? extends R> onSuccess, Function<? super Throwable, ? extends R> onFailure) {
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

    public static <A extends Object, B extends Object, C extends Object, D extends Object> Result<Tuple4<A, B, C, D>> zip(Result<A> a, Result<B> b, Result<C> c, Result<D> d) throws Throwable {
        if (Stream.of(a, b, c, d).noneMatch(it -> it.isFailure()))
            return Result.success(Tuples.of(a.getOrThrow(), b.getOrThrow(), c.getOrThrow(), d.getOrThrow()));
        else
            return Result.failure(Stream.of(a, b, c, d).filter(it -> it.isFailure()).findFirst().get().exceptionOrNull());
    }

    public static <A extends Object, B extends Object, C extends Object> Result<Tuple3<A, B, C>> zip(Result<A> a, Result<B> b, Result<C> c) throws Throwable {
        // Проверяем, что ни один из переданных Result не является неуспешным
        if (Stream.of(a, b, c).noneMatch(it -> it.isFailure())) {
            // Если все Result успешны, создаем кортеж из их значений и возвращаем успешный Result
            return Result.success(Tuples.of(a.getOrThrow(), b.getOrThrow(), c.getOrThrow()));
        } else {
            // Если хотя бы один Result неуспешен, находим первый неуспешный Result и возвращаем его исключение
            return Result.failure(Stream.of(a, b, c)
                    .filter(it -> it.isFailure()) // Фильтруем неуспешные результаты
                    .findFirst() // Находим первый неуспешный результат
                    .get() // Получаем этот результат
                    .exceptionOrNull() // Получаем исключение из этого результата
            );
        }
    }

    public <R> Result<R> flatMap(Function<? super T, ? extends Result<? extends R>> transform) {
        if (this.isSuccess()) {
            return (Result<R>) transform.apply(this.getOrNull());
        } else {
            return Result.failure(this.exceptionOrNull());
        }
    }

}
