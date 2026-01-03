package com.siesque.rilipackcore.datagen;

import com.siesque.rilipackcore.Rilipackcore;
import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import com.siesque.rilipackcore.tag.RilipackcoreTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class RilipackcoreBlockTagProvider extends BlockTagsProvider {
    public RilipackcoreBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Rilipackcore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(RilipackcoreTags.Blocks.CLEANROOM_DOORS)
                .add(RilipackcoreBlocks.CLEANROOM_DOOR.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(RilipackcoreBlocks.CLEANROOM_DOOR.get(),
                        RilipackcoreBlocks.CLEANROOM_CONTROLLER.get(),
                        RilipackcoreBlocks.PLASCRETE_CASING.get(),
                        RilipackcoreBlocks.CLEANROOM_FILTER.get(),
                        RilipackcoreBlocks.CLEANROOM_FILTER_STERILE.get(),
                        RilipackcoreBlocks.ENERGY_INPUT_HATCH.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(RilipackcoreBlocks.CLEANROOM_DOOR.get(),
                        RilipackcoreBlocks.CLEANROOM_CONTROLLER.get(),
                        RilipackcoreBlocks.PLASCRETE_CASING.get(),
                        RilipackcoreBlocks.CLEANROOM_FILTER.get(),
                        RilipackcoreBlocks.CLEANROOM_FILTER_STERILE.get(),
                        RilipackcoreBlocks.ENERGY_INPUT_HATCH.get());
    }
}
