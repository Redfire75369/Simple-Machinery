package mods.redfire.simplemachinery;

import mods.redfire.simplemachinery.registry.*;
import mods.redfire.simplemachinery.setup.ClientSetup;
import mods.redfire.simplemachinery.setup.Config;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SimpleMachinery.MODID)
public class SimpleMachinery {
    public static final String MODID = "simplemachinery";
    public static final Logger LOGGER = LogManager.getLogger();


    public static final ItemGroup TAB_BLOCKS = new ItemGroup("simplemachinery_blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.ITEM_REGOLITH.get());
        }
    };

    public static final ItemGroup TAB_MACHINES = new ItemGroup("simplemachinery_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.ITEM_TURNTABLE.get());
        }
    };


    public SimpleMachinery() {
        ModLoadingContext ctxLoading = ModLoadingContext.get();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ctxLoading.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ctxLoading.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Blocks.register(bus);
        Items.register(bus);
        TileEntities.register(bus);
        Containers.register(bus);

        bus.addGenericListener(IRecipeSerializer.class, RecipeSerializers::register);

        bus.addListener(ClientSetup::init);
    }
}
