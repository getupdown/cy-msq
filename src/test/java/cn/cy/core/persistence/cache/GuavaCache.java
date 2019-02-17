package cn.cy.core.persistence.cache;

import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCache {

    @Test
    public void testMaximumSize() {

        int max = 10;

        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(max).build();

        for (int i = 0; i < max; i++) {
            cache.put(String.valueOf(i), String.valueOf(i));
        }

        cache.put("123", "123");

    }
}
