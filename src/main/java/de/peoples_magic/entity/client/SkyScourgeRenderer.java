package de.peoples_magic.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.PhantomRenderState;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.WitherSkeleton;

public class SkyScourgeRenderer extends PhantomRenderer {
    public SkyScourgeRenderer(EntityRendererProvider.Context p_174447_) {
        super(p_174447_);
    }

    protected void scale(PhantomRenderState state, PoseStack poseStack) {
        poseStack.scale(7F, 7F, 7F);
        super.scale(state, poseStack);
    }
}
