package com.siesque.rilipackcore.datagen;

import com.siesque.rilipackcore.Rilipackcore;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Rilipackcore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), RilipackcoreLootTableProvider.create(packOutput));
        generator.addProvider(event.includeClient(), new RilipackcoreBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new RilipackcoreItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new RilipackcoreBlockTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
    }
}
