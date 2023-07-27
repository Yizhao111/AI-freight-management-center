package vip.penint.cloud.common.core.utils;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
