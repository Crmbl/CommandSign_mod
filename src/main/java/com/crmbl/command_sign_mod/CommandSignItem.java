package com.crmbl.command_sign_mod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
        if (!worldIn.isRemote && !flag && player != null) {

            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof CommandSignTileEntity)
                player.openSignEditor((CommandSignTileEntity)tileEntity);
        }

        return flag;
    }
}
