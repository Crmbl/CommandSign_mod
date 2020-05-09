package com.crmbl.command_sign_mod;

import net.minecraft.block.BlockState;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CommandSignModUpdateSignPacket implements IPacket<IServerPlayNetHandler> {
    private BlockPos pos;
    private String[] commands;
    private String[] lines;

    @OnlyIn(Dist.CLIENT)
    public CommandSignModUpdateSignPacket(BlockPos blockPos, String[] commands, String[] lines) {
        this.pos = blockPos;
        this.commands = new String[] { commands[0], commands[1], commands[2], commands[3] };
        this.lines = new String[] { lines[0], lines[1], lines[2], lines[3] };
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public String[] getCommand() { return this.commands; }

    public String[] getLines() { return this.lines; }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
        this.lines = new String[4];

        for (int i = 0; i < 4; i++)
            this.commands[i] = buf.readString(384);
        for (int i = 0; i < 4; i++)
            this.lines[i] = buf.readString(384);
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);

        for (int i = 0; i < 4; i++)
            buf.writeString(this.commands[i]);
        for (int i = 0; i < 4; i++)
            buf.writeString(this.lines[i]);
    }

    @Override
    public void processPacket(IServerPlayNetHandler handler) {
        ServerPlayNetHandler server = (ServerPlayNetHandler)handler;
        PacketThreadUtil.checkThreadAndEnqueue(this, server, server.player.getServerWorld());
        server.player.markPlayerActive();
        ServerWorld serverworld = server.player.getServer().getWorld(server.player.dimension);
        BlockPos blockpos = this.getPosition();
        if (serverworld.isBlockLoaded(blockpos)) {
            BlockState blockstate = serverworld.getBlockState(blockpos);
            TileEntity tileentity = serverworld.getTileEntity(blockpos);
            if (!(tileentity instanceof CommandSignTileEntity))
                return;

            CommandSignTileEntity commandSignTileEntity = (CommandSignTileEntity)tileentity;
            if (!commandSignTileEntity.getIsEditable() || commandSignTileEntity.getPlayer() != server.player) {
                server.player.getServer().logWarning("Player " + server.player.getName().getString() + " just tried to change non-editable sign");
                return;
            }

            String[] lines = this.getLines();
            for(int i = 0; i < lines.length; ++i)
                commandSignTileEntity.setText(i, new StringTextComponent(TextFormatting.getTextWithoutFormattingCodes(lines[i])));

            String[] commands = this.getCommand();
            for(int i = 0; i < commands.length; ++i)
                commandSignTileEntity.setCommand(i, new StringTextComponent(TextFormatting.getTextWithoutFormattingCodes(commands[i])));

            commandSignTileEntity.markDirty();
            serverworld.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
        }
    }
}