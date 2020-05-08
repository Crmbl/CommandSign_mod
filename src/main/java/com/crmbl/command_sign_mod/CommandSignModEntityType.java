package com.crmbl.command_sign_mod;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommandSignModEntityType {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, CommandSignMod.MOD_ID);
    public static final RegistryObject<TileEntityType<CommandSignTileEntity>> COMMAND_SIGN_TILE_ENTITY = TILE_ENTITY_TYPES.register("command_sign_block",
        () -> TileEntityType.Builder.create(CommandSignTileEntity::new,
                CommandSignModBlocks.COMMAND_SIGN_STANDING_BLOCK.get(),
                CommandSignModBlocks.COMMAND_SIGN_WALL_BLOCK.get()
        ).build(null)
    );
}