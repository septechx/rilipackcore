package com.siesque.rilipackcore.datagen;

import com.siesque.rilipackcore.Rilipackcore;
import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class RilipackcoreBlockStateProvider extends BlockStateProvider {
    public RilipackcoreBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Rilipackcore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(RilipackcoreBlocks.PLASCRETE_CASING);
        blockWithItem(RilipackcoreBlocks.CLEANROOM_FILTER);
        blockWithItem(RilipackcoreBlocks.CLEANROOM_FILTER_STERILE);

        directionalFrontBlock(RilipackcoreBlocks.ENERGY_INPUT_HATCH,
                "energy_hatch",
                "plascrete_casing");

        cubeBottomTop(RilipackcoreBlocks.CLEANROOM_CONTROLLER,
                "plascrete_casing",
                "controller",
                "plascrete_casing");

        doorBlockWithRenderType(
                (DoorBlock) RilipackcoreBlocks.CLEANROOM_DOOR.get(),
                modLoc("block/door/cleanroom_door_bottom"),
                modLoc("block/door/cleanroom_door_top"),
                "cutout");
    }

    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void cubeBottomTop(RegistryObject<Block> block, String side, String bottom, String top) {
        ModelFile model = models().cubeBottomTop(block.getId().getPath(),
                modLoc("block/" + side),
                modLoc("block/" + bottom),
                modLoc("block/" + top));
        simpleBlockWithItem(block.get(), model);
    }

    private void directionalFrontBlock(RegistryObject<Block> block, String front, String other) {
        ModelFile model = models().cubeBottomTop(
                block.getId().getPath(),
                modLoc("block/" + other),
                modLoc("block/" + other),
                modLoc("block/" + front)
        );

        directionalBlock(block.get(), model);
    }
}