package com.crmbl.command_sign_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CommandSignModHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("command_sign_mod","main"),() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        INSTANCE.registerMessage(id++, CommandSignModUpdateSignPacket.class, CommandSignModUpdateSignPacket::encode, CommandSignModUpdateSignPacket::decode, CommandSignModUpdateSignPacket::handle);
    }
}
