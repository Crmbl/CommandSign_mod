package com.crmbl.command_sign_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class CommandSignModOpenSignPacket {
    private final BlockPos signPosition;
    private final boolean isTextEdit;

    public CommandSignModOpenSignPacket(BlockPos posIn, boolean isTextEdit) {
        this.signPosition = posIn;
        this.isTextEdit = isTextEdit;
    }

    public CommandSignModOpenSignPacket(PacketBuffer packetBuffer) {
        this.signPosition = packetBuffer.readBlockPos();
        this.isTextEdit = packetBuffer.readBoolean();
    }

    public static CommandSignModOpenSignPacket decode(PacketBuffer buf) {
        return new CommandSignModOpenSignPacket(buf);
    }

    public void encode(PacketBuffer buf) {
        buf.writeBlockPos(this.signPosition);
        buf.writeBoolean(this.isTextEdit);
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getSignPosition() {
        return this.signPosition;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getTextEdit() { return this.isTextEdit; }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.world == null)
                return;

            TileEntity tileentity = minecraft.world.getTileEntity(this.getSignPosition());
            if (!(tileentity instanceof CommandSignTileEntity)) {
                tileentity = new CommandSignTileEntity();
                tileentity.setWorldAndPos(minecraft.world, this.getSignPosition());
            }

            CommandSignTileEntity commandSignTile = (CommandSignTileEntity)tileentity;
            Minecraft.getInstance().displayGuiScreen(new CommandSignScreen(commandSignTile, this.getTextEdit()));
        });

        context.setPacketHandled(true);
    }
}