package com.crmbl.command_sign_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CommandSignModHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("command_sign_mod","handler"),() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        INSTANCE.registerMessage(0, CommandSignModUpdateSignPacket.class, CommandSignModUpdateSignPacket::encode, CommandSignModUpdateSignPacket::decode, CommandSignModUpdateSignPacket::handle);
        INSTANCE.registerMessage(1, CommandSignModOpenSignPacket.class, CommandSignModOpenSignPacket::encode, CommandSignModOpenSignPacket::decode, CommandSignModOpenSignPacket::handle);
    }
}
