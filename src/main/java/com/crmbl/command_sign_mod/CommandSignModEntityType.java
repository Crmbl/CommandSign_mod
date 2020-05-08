package com.crmbl.command_sign_mod;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommandSignModEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, CommandSignMod.MOD_ID);
    //public static final RegistryObject<EntityType<ThrowingKnifeEntity>> THROWING_KNIFE = ENTITY_TYPES.register("throwing_knife_item", () ->
    //        EntityType.Builder.<ThrowingKnifeEntity>create(ThrowingKnifeEntity::new, EntityClassification.MISC).build("throwing_knife_item"));
}