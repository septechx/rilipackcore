package com.siesque.rilipackcore.multiblock.pattern;

import com.siesque.rilipackcore.multiblock.pattern.util.RelativeDirection;

import java.util.*;

public class FactoryBlockPattern {
    private final List<RelativeDirection> directions = new ArrayList<>();
    private final List<String[]> structure = new ArrayList<>();
    private final Map<Character, TraceabilityPredicate> predicates = new LinkedHashMap<>();
    private int repeatableLayerCount = 0;

    public static FactoryBlockPattern start(RelativeDirection... directions) {
        FactoryBlockPattern pattern = new FactoryBlockPattern();
        Collections.addAll(pattern.directions, directions);
        return pattern;
    }

    public FactoryBlockPattern aisle(String... layer) {
        structure.add(layer);
        return this;
    }

    public FactoryBlockPattern setRepeatable(int count) {
        this.repeatableLayerCount = count;
        return this;
    }

    public FactoryBlockPattern where(char symbol, TraceabilityPredicate predicate) {
        predicates.put(symbol, predicate);
        return this;
    }

    public BlockPattern build() {
        return new BlockPattern(
            directions.toArray(new RelativeDirection[0]),
            structure.toArray(new String[0][]),
            predicates
        );
    }
}