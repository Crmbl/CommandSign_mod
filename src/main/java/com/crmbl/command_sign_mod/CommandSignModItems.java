package com.crmbl.command_sign_mod;

import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommandSignModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, CommandSignMod.MOD_ID);
    public static final RegistryObject<CommandSignItem> COMMAND_SIGN = ITEMS.register("command_sign_item", () ->
            new CommandSignItem(new Item.Properties()
                    .maxStackSize(16)
                    .group(ItemGroup.DECORATIONS),
                    CommandSignModBlocks.COMMAND_SIGN_STANDING_BLOCK.get(),
                    CommandSignModBlocks.COMMAND_SIGN_WALL_BLOCK.get()
            )
    );
    public static final RegistryObject<Item> COMMAND_WAND = ITEMS.register("command_wand_item", () ->
            new CommandWandItem(new Item.Properties()
                    .maxStackSize(64)
                    .setNoRepair()
                    .group(ItemGroup.MISC)
            )
    );
}
