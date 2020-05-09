package com.crmbl.command_sign_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SOpenSignMenuPacket;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandSignModEventHandler {

    /*@SubscribeEvent
    public void handleSignEditorOpen(SOpenSignMenuPacket event) {
        PacketThreadUtil.checkThreadAndEnqueue(event, this, NetworkMa);
        TileEntity tileentity = this.world.getTileEntity(event.getSignPosition());
        if (!(tileentity instanceof SignTileEntity)) {
            tileentity = new SignTileEntity();
            tileentity.setWorldAndPos(this.world, event.getSignPosition());
        }

        this.client.player.openSignEditor((CommandSignTileEntity)tileentity);
    }*/
}
