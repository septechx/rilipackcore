package com.siesque.rilipackcore.multiblock.pattern;

import com.siesque.rilipackcore.multiblock.capability.IPartAbility;
import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public final class Predicates {
    public static TraceabilityPredicate controller(Object controller) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                // TODO: Will be implemented after IMultiblockController is created
                // For now, compare position directly
                return state.getBlockEntity() != null && controller != null;
            }

            @Override
            public boolean isAny() {
                return true;
            }
        };
    }

    public static TraceabilityPredicate states(BlockState... states) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                BlockState testState = state.getBlockState();
                for (BlockState validState : states) {
                    if (testState == validState) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static TraceabilityPredicate blocks(Block... blocks) {
        BlockState[] states = new BlockState[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            states[i] = blocks[i].defaultBlockState();
        }
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                BlockState testState = state.getBlockState();
                for (BlockState validState : states) {
                    if (testState == validState) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static TraceabilityPredicate abilities(IPartAbility... abilities) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                if (state.getBlockEntity() instanceof IMultiPart part) {
                    for (IPartAbility ability : abilities) {
                        for (IPartAbility partAbility : part.getAbilities()) {
                            if (ability.getName().equals(partAbility.getName())) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };
    }

    public static TraceabilityPredicate blockTag(TagKey<Block> tag) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                return state.getBlockState().is(tag);
            }
        };
    }

    public static TraceabilityPredicate doors(TagKey<Block> doorTag) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                BlockState blockState = state.getBlockState();
                if (!blockState.is(doorTag)) {
                    return false;
                }
                return isValidDoor(blockState);
            }
        };
    }

    private static boolean isValidDoor(BlockState state) {
        return state.hasProperty(DoorBlock.HALF) &&
                state.hasProperty(DoorBlock.FACING) &&
                state.hasProperty(DoorBlock.OPEN);
    }

    public static TraceabilityPredicate custom(BlockStatePredicate predicate, Supplier<BlockInfo[]> preview) {
        return new TraceabilityPredicate() {
            @Override
            public boolean test(IBlockWorldState state) {
                return predicate.test(state);
            }

            @Override
            public Supplier<BlockInfo[]> getPreview() {
                return preview;
            }
        };
    }

    public interface BlockStatePredicate {
        boolean test(IBlockWorldState state);
    }
}