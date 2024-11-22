package io.collective;

import java.time.Clock;
import java.time.Duration;

public class SimpleCache {

    public static final int CACHE_CAPACITY_INITIAL = 3;
    public static final int CACHE_CAPACITY_INCREASE_STEP = 2;

    private final Clock clock;
    private SimpleCacheEntry[] cache;

    public SimpleCache(Clock clock) {
        this.clock = clock;
        init();
    }

    public SimpleCache() {
        this.clock = Clock.offset(Clock.systemDefaultZone(), Duration.ZERO);
        init();
    }

    private void init() {
        cache = getNewCache(CACHE_CAPACITY_INITIAL);
    }

    public void put(Object key, Object value, int retentionInMillis) {
        Integer cacheIndex = getFirstFreeIndex(clock.millis());
        if (cacheIndex == null) {
            cacheIndex = cache.length;
            increaseCacheSize();
        }
        cache[cacheIndex] = new SimpleCacheEntry(key, value, clock.millis() + retentionInMillis);
    }

    public boolean isEmpty() {
        boolean result = true;
        for (SimpleCacheEntry simpleCacheEntry : cache) {
            if (simpleCacheEntry != null) {
                result = false;
                break;
            }
        }
        return result;
    }

    public int size() {
        int result = 0;
        for (SimpleCacheEntry simpleCacheEntry : cache) {
            if (isEntryExists(simpleCacheEntry)) {
                result++;
            }
        }
        return result;
    }

    public Object get(Object key) {
        Object result = null;
        for (SimpleCacheEntry simpleCacheEntry : cache) {
            if (isEntryExists(simpleCacheEntry) && simpleCacheEntry.getKey().equals(key)) {
                result = simpleCacheEntry.getValue();
                break;
            }
        }
        return result;
    }

    private boolean isEntryExists(SimpleCacheEntry simpleCacheEntry) {
        return (simpleCacheEntry != null && simpleCacheEntry.getExpirationInMillis() > clock.millis());
    }

    private Integer getFirstFreeIndex(long currentTimeInMills) {
        Integer result = null;
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] == null || cache[i].getExpirationInMillis() < currentTimeInMills) {
                result = i;
                break;
            }
        }
        return result;
    }

    private SimpleCacheEntry[] getNewCache(int capacity) {
        return new SimpleCacheEntry[capacity];
    }

    private void increaseCacheSize() {
        SimpleCacheEntry[] newCache = getNewCache(cache.length + CACHE_CAPACITY_INCREASE_STEP);
        System.arraycopy(cache, 0, newCache, 0, cache.length);
        cache = newCache;
    }

    public int getCurrentCacheCapacity() {
        return cache.length;
    }

}