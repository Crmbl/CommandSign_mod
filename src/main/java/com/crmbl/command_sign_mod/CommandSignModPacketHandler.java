package com.crmbl.command_sign_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class CommandSignModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("command_sign_mod","main"),() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        INSTANCE.registerMessage(0, CommandSignModOpenMenuPacket.class, CommandSignModOpenMenuPacket::encode, CommandSignModOpenMenuPacket::decode, CommandSignModOpenMenuPacket::handle);
    }
}