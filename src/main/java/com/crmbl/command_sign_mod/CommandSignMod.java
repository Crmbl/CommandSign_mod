package com.crmbl.command_sign_mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("command_sign_mod")
public class CommandSignMod
{
    public static final String MOD_ID = "command_sign_mod";

    public CommandSignMod() {
        MinecraftForge.EVENT_BUS.register(this);
        CommandSignModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CommandSignModEntityType.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}