package com.baidu.oped.apm.bootstrap.plugin;

/**
 * class Option
 *
 * @author meidongxu@baidu.com
 */
public abstract class Option<T> {
    private static final Option<Object> EMPTY = new Option<Object>() {

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    };

    public static <U> Option<U> withValue(U value) {
        return new WithValue<U>(value);
    }

    @SuppressWarnings("unchecked")
    public static <U> Option<U> empty() {
        return (Option<U>) EMPTY;
    }

    public abstract T getValue();

    public abstract boolean hasValue();

    private static final class WithValue<T> extends Option<T> {
        private final T value;

        private WithValue(T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean hasValue() {
            return true;
        }

    }

}
