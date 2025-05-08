package de.peoples_magic.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.peoples_magic.entity.mini_boss.BlazenKnight;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.world.entity.monster.WitherSkeleton;

public class BlazenKnightRenderer extends WitherSkeletonRenderer {
    public BlazenKnightRenderer(EntityRendererProvider.Context p_174447_) {
        super(p_174447_);
    }


    @Override
    protected void scale(SkeletonRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        if (state instanceof BlazenKnightRenderState bk_state) {
            if (bk_state.is_passenger) {
                poseStack.translate(0.0D, 0.6D, 0.0D); // Adjust Y-axis translation
            }
        }
    }

    @Override
    public void extractRenderState(WitherSkeleton entity, SkeletonRenderState state, float partialTick) {
        // Sets the living entity and entity render state info
        super.extractRenderState(entity, state, partialTick);
        // Set our own variables
        if (entity instanceof BlazenKnight) {
            ((BlazenKnightRenderState) state).is_passenger = entity.isPassenger();
        }
    }

    @Override
    public BlazenKnightRenderState createRenderState() {
        return new BlazenKnightRenderState();
    }
}
