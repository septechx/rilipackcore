package com.siesque.rilipackcore.mixin;

import appeng.core.definitions.AEItems;
import appeng.init.InitVillager;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InitVillager.class)
public final class AE2InitVillagerMixin {
    @Inject(
            method = "buyItems",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void onBuyItems(int minLevel, ItemLike boughtItem, int emeraldCost, int numberOfItems, int xp, CallbackInfo ci) {
        if (
                boughtItem == AEItems.CALCULATION_PROCESSOR_PRESS || boughtItem == AEItems.LOGIC_PROCESSOR_PRESS ||
                boughtItem == AEItems.ENGINEERING_PROCESSOR_PRESS || boughtItem == AEItems.SILICON_PRESS
        ) {
            ci.cancel();
        }
    }
}
