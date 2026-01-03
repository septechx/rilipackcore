package com.siesque.rilipackcore.multiblock.state;

import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import com.siesque.rilipackcore.multiblock.pattern.MatchContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public record MultiblockState(boolean formed, MatchContext matchContext, Map<BlockPos, IMultiPart> parts,
                              Set<BlockPos> cachedPositions, Component errorMessage) {
    public MultiblockState(boolean formed, MatchContext context, Map<BlockPos, IMultiPart> parts, Set<BlockPos> cache) {
        this(formed, context, parts, cache, null);
    }

    public MultiblockState(Component errorMessage) {
        this(false, null, null, null, errorMessage);
    }

    public Collection<IMultiPart> getParts() {
        return parts != null ? parts.values() : null;
    }

    public Map<BlockPos, IMultiPart> getPartsMap() {
        return parts;
    }

    public boolean contains(BlockPos pos) {
        return cachedPositions != null && cachedPositions.contains(pos);
    }
}