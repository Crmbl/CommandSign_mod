package com.crmbl.command_sign_mod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommandSignModBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, CommandSignMod.MOD_ID);
    public static final RegistryObject<Block> COMMAND_SIGN_STANDING_BLOCK = BLOCKS.register("command_sign_block", () ->
        new CommandSignBlock(Block.Properties.create(Material.WOOD)
            .doesNotBlockMovement()
            .hardnessAndResistance(1.0F)
            .sound(SoundType.WOOD),
            WoodType.OAK)
    );
    public static final RegistryObject<Block> COMMAND_SIGN_WALL_BLOCK = BLOCKS.register("command_sign_wall_block", () ->
        new CommandSignWallBlock(Block.Properties.create(Material.WOOD)
            .doesNotBlockMovement()
            .hardnessAndResistance(1.0F)
            .lootFrom(COMMAND_SIGN_STANDING_BLOCK.get())
            .sound(SoundType.WOOD),
            WoodType.OAK)
    );
}
