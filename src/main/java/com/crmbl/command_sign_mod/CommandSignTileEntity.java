package com.crmbl.command_sign_mod;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CommandSignTileEntity extends SignTileEntity {

    public final ITextComponent[] commandText = new ITextComponent[]{new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent("")};
    private final String[] renderCommand = new String[4];

    @Override
    public TileEntityType<?> getType() {
        return CommandSignModEntityType.COMMAND_SIGN_TILE_ENTITY.get();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        for(int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.toJson(this.commandText[i]);
            compound.putString("CommandSign_Command" + (i + 1), s);
        }
        compound.putString("Color", DyeColor.RED.getTranslationKey());
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        for(int i = 0; i < 4; ++i) {
            String s = compound.getString("CommandSign_Command" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            if (this.world instanceof ServerWorld) {
                try {
                    this.commandText[i] = TextComponentUtils.updateForEntity(this.getCommandSource(null), itextcomponent, null, 0);
                } catch (CommandSyntaxException var6) {
                    this.commandText[i] = itextcomponent;
                }
            } else
                this.commandText[i] = itextcomponent;

            this.renderCommand[i] = null;
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        this.read(tag);
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt) {
        super.handleUpdateTag(nbt);
        this.read(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    public ITextComponent getCommand(int line) {
        return this.commandText[line];
    }

    public void setCommand(int line, ITextComponent component) {
        this.commandText[line] = component;
        this.renderCommand[line] = null;
    }

    @OnlyIn(Dist.CLIENT)
    public String getRenderCommand(int line, Function<ITextComponent, String> function) {
        if (this.renderCommand[line] == null && this.commandText[line] != null)
            this.renderCommand[line] = function.apply(this.commandText[line]);

        return this.renderCommand[line];
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return false;
    }

    public ActionResultType onCommandSignActivated(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack currentItemStack = player.getHeldItem(handIn);
        Item currentItem = currentItemStack.getItem();

        if (worldIn.isRemote) {
            if (currentItem == CommandSignModItems.COMMAND_WAND.get())
                this.openEditor();
            else
                this.executeString();
        }

        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void openEditor() {
        Minecraft.getInstance().displayGuiScreen(new CommandSignScreen(this, false));
    }

    @OnlyIn(Dist.CLIENT)
    public void executeString() {
        Minecraft minecraft = Minecraft.getInstance();
        for (int i = 0; i < 4; i++) {
            if (this.commandText[i] == null || this.commandText[i].getString().equals(""))
                continue;

            ChatScreen chatScreen = new ChatScreen(this.commandText[i].getString());
            minecraft.displayGuiScreen(chatScreen);
            chatScreen.keyPressed(257, 0, 0);
        }
    }
}