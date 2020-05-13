package com.crmbl.command_sign_mod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CommandSignModUpdateSignPacket {
    private BlockPos pos;
    private String[] commands;

    public CommandSignModUpdateSignPacket(BlockPos blockPos, ITextComponent command1, ITextComponent command2, ITextComponent command3, ITextComponent command4) {
        this.pos = blockPos;
        this.commands = new String[] { command1.getString(), command2.getString(), command3.getString(), command4.getString() };
    }

    public CommandSignModUpdateSignPacket(PacketBuffer packetBuffer) {
        this.pos = packetBuffer.readBlockPos();
        this.commands = new String[4];
        for (int i = 0; i < 4; i++)
            this.commands[i] = packetBuffer.readString(384);
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(this.pos);
        for (int i = 0; i < 4; i++)
            packetBuffer.writeString(this.commands[i]);
    }

    public static CommandSignModUpdateSignPacket decode(PacketBuffer buf) {
        return new CommandSignModUpdateSignPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null)
                return;

            player.markPlayerActive();
            ServerWorld serverworld = player.getServer().getWorld(player.dimension);
            BlockPos blockpos = this.getPosition();
            if (serverworld.isBlockLoaded(blockpos)) {
                BlockState blockstate = serverworld.getBlockState(blockpos);
                TileEntity tileentity = serverworld.getTileEntity(blockpos);
                if (!(tileentity instanceof CommandSignTileEntity))
                    return;

                CommandSignTileEntity commandSignTileEntity = (CommandSignTileEntity)tileentity;
                String[] commands = this.getCommand();
                for(int i = 0; i < commands.length; ++i)
                    commandSignTileEntity.setCommand(i, new StringTextComponent(TextFormatting.getTextWithoutFormattingCodes(commands[i])));

                commandSignTileEntity.markDirty();
                serverworld.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
            }
        });

        context.setPacketHandled(true);
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public String[] getCommand() { return this.commands; }
}