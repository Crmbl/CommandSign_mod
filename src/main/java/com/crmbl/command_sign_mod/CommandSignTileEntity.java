package com.crmbl.command_sign_mod;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
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
import net.minecraft.network.play.ServerPlayNetHandler;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CommandSignTileEntity extends SignTileEntity {

    public final ITextComponent[] signText = new ITextComponent[]{new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent("")};
    public final ITextComponent[] commandText = new ITextComponent[]{new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent("")};
    private boolean isEditable = true;
    private PlayerEntity player;
    private final String[] renderText = new String[4];
    private final String[] renderCommand = new String[4];
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
        for(int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.toJson(this.commandText[i]);
            compound.putString("CommandSign_Command" + (i + 1), s);
        }

        compound.putString("Color", this.textColor.getTranslationKey());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.isEditable = false;
        this.textColor = DyeColor.byTranslationKey(compound.getString("Color"), DyeColor.RED);

        for(int i = 0; i < 4; ++i) {
            String s = compound.getString("CommandSign_Command" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            if (this.world instanceof ServerWorld) {
                try {
                    this.commandText[i] = TextComponentUtils.updateForEntity(this.getCommandSource(null), itextcomponent, null, 0);
                } catch (CommandSyntaxException var6) {
                    this.commandText[i] = itextcomponent;
                }
            } else {
                this.commandText[i] = itextcomponent;
            }

            this.renderCommand[i] = null;
        }

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

        for(int i = 0; i < 4; ++i) {
            String s = tag.getString("CommandSign_Command" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            if (this.world instanceof ServerWorld) {
                try {
                    this.commandText[i] = TextComponentUtils.updateForEntity(this.getCommandSource(null), itextcomponent, null, 0);
                } catch (CommandSyntaxException var6) {
                    this.commandText[i] = itextcomponent;
                }
            } else {
                this.commandText[i] = itextcomponent;
            }

            this.renderCommand[i] = null;
        }
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
    public ITextComponent getCommand(int line) {
        return this.commandText[line];
    }

    @Override
    public void setText(int line, ITextComponent component) {
        this.signText[line] = component;
        this.renderText[line] = null;
    }

    public void setCommand(int line, ITextComponent component) {
        this.commandText[line] = component;
        this.renderCommand[line] = null;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getRenderText(int line, Function<ITextComponent, String> function) {
        if (this.renderText[line] == null && this.signText[line] != null)
            this.renderText[line] = function.apply(this.signText[line]);

        return this.renderText[line];
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
    public void executeString(ServerPlayerEntity playerIn) {
        MinecraftServer serverWorld = playerIn.getServerWorld().getServer();
        Commands commandManager = serverWorld.getCommandManager();

        //TODO get all lines and put on one line
        //this.commandText = "time set day";
        String finalCommand = "time set day";
        try {
            commandManager.getDispatcher().execute(finalCommand, serverWorld.getCommandSource());
        } catch (CommandSyntaxException ignored) {
            serverWorld.sendMessage(new TranslationTextComponent("command_sign_mod.syntax_error"));
        }
    }

    public ActionResultType onCommandSignActivated(ServerPlayerEntity player, Hand handIn) {
        ItemStack currentItemStack = player.getHeldItem(handIn);
        Item currentItem = currentItemStack.getItem();

        if (currentItem == CommandSignModItems.COMMAND_WAND.get()) {
            this.isEditable = true;
            this.setPlayer(player);
            player.connection.sendPacket(new CommandSignModOpenSignPacket(this.getPos(), false));
        }
        else
            this.executeString(player);

        return ActionResultType.SUCCESS;
    }
    //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\
}