package com.siesque.rilipackcore.multiblock.pattern;

import java.util.function.Supplier;

public abstract class TraceabilityPredicate {
    protected final MatchContext matchContext;
    protected boolean isAny = false;
    protected boolean addCache = false;
    protected int minGlobalLimit = 0;
    protected int maxGlobalLimit = Integer.MAX_VALUE;
    protected Supplier<BlockInfo[]> preview;

    public TraceabilityPredicate() {
        this(new MatchContext());
    }

    public TraceabilityPredicate(MatchContext matchContext) {
        this.matchContext = matchContext;
    }

    public abstract boolean test(IBlockWorldState state);

    public TraceabilityPredicate or(TraceabilityPredicate other) {
        return new OrPredicate(this, other);
    }

    public TraceabilityPredicate and(TraceabilityPredicate other) {
        return new AndPredicate(this, other);
    }

    public TraceabilityPredicate setMinGlobalLimited(int min) {
        this.minGlobalLimit = min;
        return this;
    }

    public TraceabilityPredicate setMaxGlobalLimited(int max) {
        this.maxGlobalLimit = max;
        return this;
    }

    public boolean isAny() {
        return isAny;
    }

    public boolean addCache() {
        return addCache;
    }

    public MatchContext getMatchContext() {
        return matchContext;
    }

    public Supplier<BlockInfo[]> getPreview() {
        return preview;
    }

    private static class OrPredicate extends TraceabilityPredicate {
        private final TraceabilityPredicate left;
        private final TraceabilityPredicate right;

        OrPredicate(TraceabilityPredicate left, TraceabilityPredicate right) {
            super();
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean test(IBlockWorldState state) {
            return left.test(state) || right.test(state);
        }

        @Override
        public TraceabilityPredicate setMinGlobalLimited(int min) {
            super.setMinGlobalLimited(min);
            left.setMinGlobalLimited(min);
            right.setMinGlobalLimited(min);
            return this;
        }

        @Override
        public TraceabilityPredicate setMaxGlobalLimited(int max) {
            super.setMaxGlobalLimited(max);
            left.setMaxGlobalLimited(max);
            right.setMaxGlobalLimited(max);
            return this;
        }
    }

    private static class AndPredicate extends TraceabilityPredicate {
        private final TraceabilityPredicate left;
        private final TraceabilityPredicate right;

        AndPredicate(TraceabilityPredicate left, TraceabilityPredicate right) {
            super();
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean test(IBlockWorldState state) {
            return left.test(state) && right.test(state);
        }

        @Override
        public TraceabilityPredicate setMinGlobalLimited(int min) {
            super.setMinGlobalLimited(min);
            left.setMinGlobalLimited(min);
            right.setMinGlobalLimited(min);
            return this;
        }

        @Override
        public TraceabilityPredicate setMaxGlobalLimited(int max) {
            super.setMaxGlobalLimited(max);
            left.setMaxGlobalLimited(max);
            right.setMaxGlobalLimited(max);
            return this;
        }
    }
}