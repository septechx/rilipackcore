package com.siesque.rilipackcore;

import com.mojang.logging.LogUtils;
import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import com.siesque.rilipackcore.block.entity.RilipackcoreBlockEntities;
import com.siesque.rilipackcore.item.RilipackcoreItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Rilipackcore.MODID)
public class Rilipackcore {
    public static final String MODID = "rilipackcore";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final IEventBus EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public Rilipackcore() {
        RilipackcoreBlocks.register();
        RilipackcoreItems.register();
        RilipackcoreBlockEntities.register();
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}