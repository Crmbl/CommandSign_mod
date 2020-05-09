package com.crmbl.command_sign_mod;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SOpenSignMenuPacket;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CommandSignTileEntity extends SignTileEntity {

    public final ITextComponent[] signText = new ITextComponent[]{new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent("")};
    public String commandText = "";
    private boolean isEditable = true;
    private PlayerEntity player;
    private final String[] renderText = new String[4];
    private DyeColor textColor = DyeColor.RED;

    @Override
    public TileEntityType<?> getType() {
        return CommandSignModEntityType.COMMAND_SIGN_TILE_ENTITY.get();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        for(int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.toJson(this.signText[i]);
            compound.putString("Text" + (i + 1), s);
        }

        if (this.commandText != null)
            compound.putString("CommandSign_Command", this.commandText);

        compound.putString("Color", this.textColor.getTranslationKey());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.isEditable = false;
        this.textColor = DyeColor.byTranslationKey(compound.getString("Color"), DyeColor.RED);
        this.commandText = compound.getString("CommandSign_Command");

        for(int i = 0; i < 4; ++i) {
            String s = compound.getString("Text" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            if (this.world instanceof ServerWorld) {
                try {
                    this.signText[i] = TextComponentUtils.updateForEntity(this.getCommandSource(null), itextcomponent, null, 0);
                } catch (CommandSyntaxException var6) {
                    this.signText[i] = itextcomponent;
                }
            } else {
                this.signText[i] = itextcomponent;
            }

            this.renderText[i] = null;
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        this.commandText = tag.getString("CommandSign_Command");
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt) {
        super.handleUpdateTag(nbt);
        this.read(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getText(int line) {
        return this.signText[line];
    }

    @OnlyIn(Dist.CLIENT)
    public String getCommand() { return this.commandText;}

    @Override
    public void setText(int line, ITextComponent p_212365_2_) {
        this.signText[line] = p_212365_2_;
        this.renderText[line] = null;
    }

    public void setCommand(String command) {
        this.commandText = command;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getRenderText(int line, Function<ITextComponent, String> p_212364_2_) {
        if (this.renderText[line] == null && this.signText[line] != null) {
            this.renderText[line] = p_212364_2_.apply(this.signText[line]);
        }

        return this.renderText[line];
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return false;
    }

    @Override
    public boolean getIsEditable() {
        return this.isEditable;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setEditable(boolean isEditableIn) {
        this.isEditable = isEditableIn;
        if (!isEditableIn) {
            this.player = null;
        }
    }

    @Override
    public void setPlayer(PlayerEntity playerIn) {
        this.player = playerIn;
    }

    @Override
    public PlayerEntity getPlayer() {
        return this.player;
    }

    @Override
    public boolean executeCommand(PlayerEntity playerIn) {
        for(ITextComponent itextcomponent : this.signText) {
            Style style = itextcomponent == null ? null : itextcomponent.getStyle();
            if (style != null && style.getClickEvent() != null) {
                ClickEvent clickevent = style.getClickEvent();
                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    playerIn.getServer().getCommandManager().handleCommand(this.getCommandSource((ServerPlayerEntity)playerIn), clickevent.getValue());
                }
            }
        }

        return true;
    }

    @Override
    public CommandSource getCommandSource(@Nullable ServerPlayerEntity playerIn) {
        String s = playerIn == null ? "Sign" : playerIn.getName().getString();
        ITextComponent itextcomponent = playerIn == null ? new StringTextComponent("Sign") : playerIn.getDisplayName();
        return new CommandSource(ICommandSource.DUMMY, new Vec3d((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D), Vec2f.ZERO, (ServerWorld)this.world, 2, s, itextcomponent, this.world.getServer(), playerIn);
    }

    @Override
    public DyeColor getTextColor() {
        return this.textColor;
    }

    @Override
    public boolean setTextColor(DyeColor newColor) {
        return false;
    }

    //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
    public void executeString(PlayerEntity playerIn) {
        if (playerIn instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerIn;
            MinecraftServer serverWorld = serverPlayer.getServerWorld().getServer();
            Commands commandManager = serverWorld.getCommandManager();

            //TODO remove
            this.commandText = "time set day";
            try {
                commandManager.getDispatcher().execute(this.commandText, serverWorld.getCommandSource());
            } catch (CommandSyntaxException ignored) {
                serverWorld.sendMessage(new TranslationTextComponent("command_sign_mod.syntax_error"));
            }
        }
    }

    public ActionResultType onCommandSignActivated(PlayerEntity player, Hand handIn) {
        ItemStack currentItemStack = player.getHeldItem(handIn);
        Item currentItem = currentItemStack.getItem();

        if (currentItem == CommandSignModItems.COMMAND_WAND.get()) {
            if (player instanceof ServerPlayerEntity) {
                this.setPlayer(player);
                CommandSignModPacketHandler.INSTANCE.send(PacketDistributor.SERVER.with(() -> null), new CommandSignModOpenMenuPacket(this.getPos(), false));
            }
        }
        else
            this.executeString(player);

        return ActionResultType.SUCCESS;
    }

    public void onCloseGUI() {
        this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 2);
    }
    //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
}