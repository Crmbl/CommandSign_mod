package com.crmbl.command_sign_mod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CommandSignModUpdateSignPacket implements IPacket<IServerPlayNetHandler> {
    private BlockPos pos;
    private String[] commands;

    public CommandSignModUpdateSignPacket() {
    }

    @OnlyIn(Dist.CLIENT)
    public CommandSignModUpdateSignPacket(BlockPos blockPos, ITextComponent command1, ITextComponent command2, ITextComponent command3, ITextComponent command4) {
        this.pos = blockPos;
        this.commands = new String[] { command1.getString(), command2.getString(), command3.getString(), command4.getString() };
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
        this.commands = new String[4];
        for (int i = 0; i < 4; i++)
            this.commands[i] = buf.readString(384);
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);

        for (int i = 0; i < 4; i++)
            buf.writeString(this.commands[i]);
    }

    @Override
    public void processPacket(IServerPlayNetHandler handler) {
        ServerPlayNetHandler server = (ServerPlayNetHandler)handler;
        ServerPlayerEntity player = server.player;
        PacketThreadUtil.checkThreadAndEnqueue(this, handler, player.getServerWorld());
        player.markPlayerActive();
        ServerWorld serverworld = player.getServer().getWorld(player.dimension);
        BlockPos blockpos = this.getPosition();
        if (serverworld.isBlockLoaded(blockpos)) {
            BlockState blockstate = serverworld.getBlockState(blockpos);
            TileEntity tileentity = serverworld.getTileEntity(blockpos);
            if (!(tileentity instanceof CommandSignTileEntity))
                return;

            CommandSignTileEntity commandSignTileEntity = (CommandSignTileEntity)tileentity;
            if (!commandSignTileEntity.getIsEditable() || commandSignTileEntity.getPlayer() != player) {
                player.getServer().logWarning("Player " + player.getName().getString() + " just tried to change non-editable sign");
                return;
            }

            String[] commands = this.getCommand();
            for(int i = 0; i < commands.length; ++i)
                commandSignTileEntity.setCommand(i, new StringTextComponent(TextFormatting.getTextWithoutFormattingCodes(commands[i])));

            commandSignTileEntity.markDirty();
            serverworld.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
        }
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public String[] getCommand() { return this.commands; }
}