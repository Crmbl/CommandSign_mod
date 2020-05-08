package com.crmbl.command_sign_mod;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class CommandSignModCustomMaterial extends Material {
    private static final ResourceLocation TEXTURE_BLOCK = new ResourceLocation("command_sign_mod:textures/entity/signs/command_sign_block.png");
    private RenderType renderType;

    public CommandSignModCustomMaterial(ResourceLocation atlasLocationIn, ResourceLocation textureLocationIn) {
        super(atlasLocationIn, textureLocationIn);
    }

    @Override
    public RenderType getRenderType(Function<ResourceLocation, RenderType> renderTypeGetter) {
        if (this.renderType == null)
            this.renderType = renderTypeGetter.apply(TEXTURE_BLOCK);

        return this.renderType;
    }

    @Override
    public IVertexBuilder getBuffer(IRenderTypeBuffer bufferIn, Function<ResourceLocation, RenderType> renderTypeGetter) {
        return ItemRenderer.getBuffer(bufferIn, this.getRenderType(renderTypeGetter), false, false);
    }
}