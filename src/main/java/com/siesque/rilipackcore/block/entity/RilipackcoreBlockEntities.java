package com.siesque.rilipackcore.block.entity;

import com.siesque.rilipackcore.Rilipackcore;
import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import com.siesque.rilipackcore.block.entity.multiblock.CleanroomControllerEntity;
import com.siesque.rilipackcore.block.entity.multiblock.EnergyInputHatchEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RilipackcoreBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Rilipackcore.MODID);

    public static void register() {
        BLOCK_ENTITIES.register(Rilipackcore.EVENT_BUS);
    }

    public static final RegistryObject<BlockEntityType<CleanroomControllerEntity>> CLEANROOM_CONTROLLER =
            BLOCK_ENTITIES.register("cleanroom_controller",
                    () -> BlockEntityType.Builder.of(
                            CleanroomControllerEntity::new,
                            RilipackcoreBlocks.CLEANROOM_CONTROLLER.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<EnergyInputHatchEntity>> ENERGY_INPUT_HATCH =
            BLOCK_ENTITIES.register("energy_input_hatch",
                    () -> BlockEntityType.Builder.of(
                            EnergyInputHatchEntity::new,
                            RilipackcoreBlocks.ENERGY_INPUT_HATCH.get()
                    ).build(null));
}