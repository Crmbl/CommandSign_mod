package com.crmbl.command_sign_mod;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

public class CommandSignWallBlock extends WallSignBlock {

    public CommandSignWallBlock(Properties properties, WoodType type) {
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        LogManager.getLogger().info("ACTIVATED WALL");

        return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
    }
}
