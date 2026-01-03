package com.siesque.rilipackcore.item;

import com.siesque.rilipackcore.Rilipackcore;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RilipackcoreItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Rilipackcore.MODID);

    public static void register() {
        ITEMS.register(Rilipackcore.EVENT_BUS);
    }
}
