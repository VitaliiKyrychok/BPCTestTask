package test.collective;

import io.collective.SimpleCache;
import org.junit.Test;

import java.time.Duration;

import static java.lang.String.format;
import static junit.framework.TestCase.assertEquals;

public class SimpleCacheExtendedTest extends SimpleCacheTest {

    @Override
    public void before() {
        super.before();
        nonempty.put("aKey", "aValue2", 3000);
    }

    @Test
    @Override
    public void size() {
        assertEquals(0, empty.size());
        assertEquals(3, nonempty.size());
    }

    @Test
    public void isCacheIncreaseCorrectly() {
        SimpleCache cache = new SimpleCache();
        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL, cache.getCurrentCacheCapacity());
        for (int i = 0; i < SimpleCache.CACHE_CAPACITY_INITIAL + 1; i++) {
            cache.put(format("aKey%d", i), format("aValue%d", i), 3000);
        }
        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL + SimpleCache.CACHE_CAPACITY_INCREASE_STEP, cache.getCurrentCacheCapacity());
    }

    @Test
    public void isCacheNotIncreaseCorrectly() {
        TestClock clock = new TestClock();
        SimpleCache cache = new SimpleCache(clock);

        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL, cache.getCurrentCacheCapacity());
        for (int i = 0; i < SimpleCache.CACHE_CAPACITY_INITIAL; i++) {
            cache.put(format("aKey%d", i), format("aValue%d", i), i * 1000);
        }
        clock.offset(Duration.ofMillis(1010));
        cache.put("new", "aValue", 2000);
        assertEquals(SimpleCache.CACHE_CAPACITY_INITIAL, cache.getCurrentCacheCapacity());
    }

}