package de.peoples_magic.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.peoples_magic.entity.mini_boss.ForestGuardian;
import net.minecraft.client.renderer.entity.CreakingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.client.renderer.entity.state.CreakingRenderState;
import net.minecraft.client.renderer.entity.state.PhantomRenderState;

public class ForestGuardianRenderer extends CreakingRenderer<ForestGuardian> {
    public ForestGuardianRenderer(EntityRendererProvider.Context p_174447_) {
        super(p_174447_);
    }

    protected void scale(ForestGuardianRenderState state, PoseStack poseStack) {
        // Apply translation for sitting height adjustment
        poseStack.scale(7F, 7F, 7F);
        super.scale(state, poseStack);
    }
}
