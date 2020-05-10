package com.crmbl.command_sign_mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("command_sign_mod")
public class CommandSignMod
{
    public static final String MOD_ID = "command_sign_mod";

    public CommandSignMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.register(this);
        CommandSignModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CommandSignModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CommandSignModEntityType.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        new CommandSignModHandler().register();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(CommandSignModEntityType.COMMAND_SIGN_TILE_ENTITY.get(), CommandSignTileEntityRenderer::new);
    }
}