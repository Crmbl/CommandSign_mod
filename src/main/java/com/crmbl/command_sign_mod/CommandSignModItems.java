package com.crmbl.command_sign_mod;

import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommandSignModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, CommandSignMod.MOD_ID);
    //public static final RegistryObject<ThrowingKnifeItem> THROWING_KNIFE = ITEMS.register("throwing_knife_item", () ->
    //        new ThrowingKnifeItem(new Item.Properties()
    //                .maxStackSize(64)
    //                .group(ItemGroup.COMBAT)
    //        )
    //);
    //public static final RegistryObject<ExplosiveArrowItem> EXPLOSIVE_ARROW = ITEMS.register("explosive_arrow_item", () ->
    //        new ExplosiveArrowItem(new Item.Properties()
    //                .maxStackSize(64)
    //                .group(ItemGroup.COMBAT)
    //        )
    //);
    //public static final RegistryObject<HealingArrowItem> HEALING_ARROW = ITEMS.register("healing_arrow_item", () ->
    //        new HealingArrowItem(new Item.Properties()
    //                .maxStackSize(64)
    //                .group(ItemGroup.COMBAT)));
}
