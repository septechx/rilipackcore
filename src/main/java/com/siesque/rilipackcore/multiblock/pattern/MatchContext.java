package com.siesque.rilipackcore.multiblock.pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MatchContext {
    private final Map<String, Object> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        return type.isInstance(value) ? (T) value : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(String key, Supplier<T> supplier) {
        return (T) data.computeIfAbsent(key, k -> supplier.get());
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}