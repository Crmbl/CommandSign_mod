package com.crmbl.command_sign_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CommandSignModOpenMenuPacket {
    private final BlockPos signPosition;
    private final boolean isTextEdit;

    public CommandSignModOpenMenuPacket(PacketBuffer packetBuffer) {
        this.signPosition = packetBuffer.readBlockPos();
        this.isTextEdit = packetBuffer.readBoolean();
    }

    public CommandSignModOpenMenuPacket(BlockPos posIn, boolean value) {
        this.signPosition = posIn;
        this.isTextEdit = value;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getSignPosition() {
        return this.signPosition;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getTextEdit() {
        return this.isTextEdit;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(signPosition);
        packetBuffer.writeBoolean(isTextEdit);
    }

    public static CommandSignModOpenMenuPacket decode(PacketBuffer buf) {
        return new CommandSignModOpenMenuPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null) {
                TileEntity tileentity = Minecraft.getInstance().world.getTileEntity(this.getSignPosition());
                CommandSignTileEntity commandSignTile = (CommandSignTileEntity)tileentity;
                Minecraft.getInstance().displayGuiScreen(new CommandSignScreen(commandSignTile, this.getTextEdit()));
            }
        });

        context.setPacketHandled(true);
    }
}
