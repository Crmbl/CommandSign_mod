package com.crmbl.command_sign_mod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CommandSignItem extends WallOrFloorItem {
    public CommandSignItem(Item.Properties propertiesIn, Block floorBlockIn, Block wallBlockIn) {
        super(floorBlockIn, wallBlockIn, propertiesIn);
    }

    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        boolean flag = super.onBlockPlaced(pos, worldIn, player, stack, state);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CommandSignTileEntity) {
            CommandSignTileEntity commandSignTile = (CommandSignTileEntity)tileEntity;
            if (player instanceof ServerPlayerEntity)
                commandSignTile.setPlayer(player); //this.connection.sendPacket(new SOpenSignMenuPacket(signTile.getPos())); ??
            if (player instanceof ClientPlayerEntity)
                Minecraft.getInstance().displayGuiScreen(new CommandSignScreen(commandSignTile, true));
        }
        return flag;
    }
}
