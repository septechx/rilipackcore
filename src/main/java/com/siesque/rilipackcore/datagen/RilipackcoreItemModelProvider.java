package com.siesque.rilipackcore.datagen;

import com.siesque.rilipackcore.Rilipackcore;
import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RilipackcoreItemModelProvider extends ItemModelProvider {
    public RilipackcoreItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Rilipackcore.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(RilipackcoreBlocks.ENERGY_INPUT_HATCH.getId().getPath(), modLoc("block/energy_input_hatch"));

        withExistingParent(RilipackcoreBlocks.CLEANROOM_DOOR.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/cleanroom_door"));
    }
}