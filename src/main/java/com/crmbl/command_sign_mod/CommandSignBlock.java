package com.crmbl.command_sign_mod;

import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CommandSignBlock extends StandingSignBlock {

    public CommandSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return CommandSignModEntityType.COMMAND_SIGN_TILE_ENTITY.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult result) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof CommandSignTileEntity))
            return super.onBlockActivated(state, worldIn, pos, player, handIn, result);

        CommandSignTileEntity signTileEntity = ((CommandSignTileEntity) tileEntity);
        return signTileEntity.onCommandSignActivated(player, handIn);
    }
}