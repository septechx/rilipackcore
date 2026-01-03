package com.siesque.rilipackcore.datagen.loot;

import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

@MethodsReturnNonnullByDefault
public class RilipackcoreBlockLootTables extends BlockLootSubProvider {
    public RilipackcoreBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(RilipackcoreBlocks.CLEANROOM_CONTROLLER.get());
        this.dropSelf(RilipackcoreBlocks.CLEANROOM_FILTER.get());
        this.dropSelf(RilipackcoreBlocks.CLEANROOM_FILTER_STERILE.get());
        this.dropSelf(RilipackcoreBlocks.ENERGY_INPUT_HATCH.get());
        this.dropSelf(RilipackcoreBlocks.PLASCRETE_CASING.get());

        this.add(RilipackcoreBlocks.CLEANROOM_DOOR.get(),
                block -> createDoorTable(RilipackcoreBlocks.CLEANROOM_DOOR.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return RilipackcoreBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
