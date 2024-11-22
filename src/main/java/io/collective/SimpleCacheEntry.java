package io.collective;

public class SimpleCacheEntry {
    private final Object key;
    private final Object value;
    private final long expirationInMillis;

    public SimpleCacheEntry(Object key, Object value, long expirationInMillis) {
        this.key = key;
        this.value = value;
        this.expirationInMillis = expirationInMillis;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public long getExpirationInMillis() {
        return expirationInMillis;
    }
}
