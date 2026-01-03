package com.siesque.rilipackcore.datagen;

import com.siesque.rilipackcore.datagen.loot.RilipackcoreBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class RilipackcoreLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(RilipackcoreBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
