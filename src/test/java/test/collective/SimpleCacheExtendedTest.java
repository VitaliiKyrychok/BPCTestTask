package test.collective;

import io.collective.SimpleCache;
import org.junit.Test;

import java.time.Duration;

import static java.lang.String.format;
import static junit.framework.TestCase.assertEquals;

public class SimpleCacheExtendedTest {

    @Test
    public void When_CacheHasDuplicateEntryKey_Expect_CorrectlyBehavior() {
        SimpleCache cache = new SimpleCache();
        cache.put("aKey", "aValue", 2000);
        cache.put("anotherKey", "anotherValue", 4000);
        cache.put("aKey", "aValue2", 3000);
        assertEquals(3, cache.size());
    }

    @Test
    public void When_CacheIncrease_Expect_CorrectlyCacheIncrease() {
        SimpleCache cache = new SimpleCache();
        for (int i = 0; i < SimpleCache.CACHE_CAPACITY_INITIAL + 1; i++) {
            cache.put(format("aKey%d", i), format("aValue%d", i), 3000);
        }
        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL + SimpleCache.CACHE_CAPACITY_INCREASE_STEP, cache.getCurrentCacheCapacity());
    }

    @Test
    public void When_CacheIncrease_Expect_CorrectlyCacheNotIncrease() {
        SimpleCacheTest.TestClock clock = new SimpleCacheTest.TestClock();
        SimpleCache cache = new SimpleCache(clock);
        for (int i = 0; i < SimpleCache.CACHE_CAPACITY_INITIAL; i++) {
            cache.put(format("aKey%d", i), format("aValue%d", i), i * 1000);
        }
        clock.offset(Duration.ofMillis(1010));
        cache.put("new", "aValue", 2000);
        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL, cache.getCurrentCacheCapacity());
    }

}