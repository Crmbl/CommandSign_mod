package com.crmbl.command_sign_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CommandSignModOpenSignPacket implements IPacket<IClientPlayNetHandler> {
    private BlockPos signPosition;
    private boolean isTextEdit;

    public CommandSignModOpenSignPacket(BlockPos posIn, boolean isTextEdit) {
        this.signPosition = posIn;
        this.isTextEdit = isTextEdit;
    }

    public void readPacketData(PacketBuffer buf) {
        this.signPosition = buf.readBlockPos();
        this.isTextEdit = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) {
        buf.writeBlockPos(this.signPosition);
        buf.writeBoolean(this.isTextEdit);
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getSignPosition() {
        return this.signPosition;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getTextEdit() { return this.isTextEdit; }

    public void processPacket(IClientPlayNetHandler handler) {
        ClientPlayNetHandler clientHandler = (ClientPlayNetHandler)handler;
        Minecraft minecraft = Minecraft.getInstance();
        PacketThreadUtil.checkThreadAndEnqueue(this, clientHandler, minecraft);
        TileEntity tileentity = minecraft.world.getTileEntity(this.getSignPosition());
        if (!(tileentity instanceof CommandSignTileEntity)) {
            tileentity = new CommandSignTileEntity();
            tileentity.setWorldAndPos(minecraft.world, this.getSignPosition());
        }

        CommandSignTileEntity commandSignTile = (CommandSignTileEntity)tileentity;
        Minecraft.getInstance().displayGuiScreen(new CommandSignScreen(commandSignTile, this.getTextEdit()));
    }
}