package com.siesque.rilipackcore.tag;

import com.siesque.rilipackcore.Rilipackcore;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class RilipackcoreTags {
    public static class Blocks {
        public static final TagKey<Block> CLEANROOM_DOORS = tag("cleanroom_doors");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(Rilipackcore.rl(name));
        }
    }

    public static class Items {

    }
}
