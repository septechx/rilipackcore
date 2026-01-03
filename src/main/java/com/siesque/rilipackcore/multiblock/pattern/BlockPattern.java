package com.siesque.rilipackcore.multiblock.pattern;

import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import com.siesque.rilipackcore.multiblock.state.MultiblockState;
import com.siesque.rilipackcore.multiblock.pattern.util.RelativeDirection;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class BlockPattern {
    private final RelativeDirection[] aisleDirections;
    private final String[][] structure;
    private final Map<Character, TraceabilityPredicate> predicates;
    private final int layerCount;
    private final int rowCount;
    private final int colCount;

    // Anchor position in the pattern (layer, row, col)
    private int anchorLayer = 0;
    private int anchorRow = 0;
    private int anchorCol = 0;

    public BlockPattern(RelativeDirection[] directions, String[][] structure, Map<Character, TraceabilityPredicate> predicates) {
        this.aisleDirections = directions;
        this.structure = structure;
        this.predicates = predicates;
        this.layerCount = structure.length;
        this.rowCount = structure.length > 0 ? structure[0].length : 0;
        this.colCount = structure.length > 0 && structure[0].length > 0 ? structure[0][0].length() : 0;
        
        findAnchor();
    }

    private void findAnchor() {
        for (int l = 0; l < layerCount; l++) {
            for (int r = 0; r < rowCount; r++) {
                String rowStr = structure[l][r];
                for (int c = 0; c < rowStr.length(); c++) {
                    if (rowStr.charAt(c) == 'C') {
                        this.anchorLayer = l;
                        this.anchorRow = r;
                        this.anchorCol = c;
                        return;
                    }
                }
            }
        }
    }

    public MultiblockState match(Level world, BlockPos center, Direction facing, Direction upDirection, boolean isFlipped) {
        MatchContext context = new MatchContext();
        Map<BlockPos, IMultiPart> parts = new HashMap<>();
        Set<BlockPos> cachedPositions = new HashSet<>();

        Component error = checkPatternAt(world, center, facing, upDirection, isFlipped, context, parts, cachedPositions);
        
        if (error == null) {
            return new MultiblockState(true, context, parts, cachedPositions, null);
        }

        return new MultiblockState(error);
    }

    private Component checkPatternAt(Level world, BlockPos center, Direction facing, Direction upDirection, boolean isFlipped,
                                   MatchContext context, Map<BlockPos, IMultiPart> parts, Set<BlockPos> cachedPositions) {
        
        Direction dirLayer = aisleDirections[0].getRelative(facing, upDirection, isFlipped);
        Direction dirRow = aisleDirections[1].getRelative(facing, upDirection, isFlipped);
        Direction dirCol = aisleDirections[2].getRelative(facing, upDirection, isFlipped);

        for (int l = 0; l < layerCount; l++) {
            for (int r = 0; r < rowCount; r++) {
                String rowStr = structure[l][r];
                for (int c = 0; c < rowStr.length(); c++) {
                    char symbol = rowStr.charAt(c);
                    TraceabilityPredicate predicate = predicates.get(symbol);
                    if (predicate == null) continue;

                    // Calculate offset from anchor
                    int dL = l - anchorLayer;
                    int dR = r - anchorRow;
                    int dC = c - anchorCol;

                    BlockPos targetPos = center
                            .relative(dirLayer, dL)
                            .relative(dirRow, dR)
                            .relative(dirCol, dC);

                    BlockWorldState worldState = new BlockWorldState(world, targetPos, facing, upDirection, isFlipped);

                    if (!predicate.test(worldState)) {
                        BlockState state = world.getBlockState(targetPos);
                        return Component.literal("Invalid block at ")
                                .append(Component.literal(targetPos.toShortString()).withStyle(ChatFormatting.YELLOW))
                                .append(" (Pattern: '" + symbol + "'). Found: ")
                                .append(state.getBlock().getName().withStyle(ChatFormatting.RED));
                    }

                    cachedPositions.add(targetPos);

                    if (worldState.getBlockEntity() instanceof IMultiPart part) {
                        parts.put(targetPos, part);
                    }
                    
                    // Capture special blocks if needed by the predicate or context (logic not fully visible here but assuming standard behavior)
                    // If context capturing is needed, it should be done here.
                    // For now, minimal changes to support structure.
                }
            }
        }

        return null;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public int getWidth() {
        return colCount;
    }

    public int getDepth() {
        return rowCount; // Depth usually refers to rows in this context
    }
}