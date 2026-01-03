package com.siesque.rilipackcore.block.multiblock;

import com.siesque.rilipackcore.multiblock.cleanroom.CleanroomType;
import net.minecraft.world.level.block.Block;

public class FilterBlock extends Block {
    private final CleanroomType cleanroomType;

    public FilterBlock(Properties properties, CleanroomType cleanroomType) {
        super(properties);
        this.cleanroomType = cleanroomType;
    }

    public CleanroomType getCleanroomType() {
        return cleanroomType;
    }
}