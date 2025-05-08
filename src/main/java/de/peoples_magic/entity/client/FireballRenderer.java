package de.peoples_magic.entity.client;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.peoples_magic.entity.spells.AetherGripProjectile;
import de.peoples_magic.entity.spells.FireballProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FireballRenderer extends EntityRenderer<FireballProjectile, FireballRenderState> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
    private final FireballProjectileModel model;

    public FireballRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FireballProjectileModel(context.bakeLayer(FireballProjectileModel.LAYER_LOCATION));
    }


    @Override
    public void extractRenderState(FireballProjectile entity, FireballRenderState state, float partialTick) {
        // Sets the living entity and entity render state info
        super.extractRenderState(entity, state, partialTick);
        // Set our own variables
        state.tick_count = entity.tickCount;
        state.camera_dist_sqr = this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity);
        state.y_rot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.x_rot = entity.getXRot() * 4f;
        state.rendering_rot = entity.getRenderingRotation() * -5f;
    }


    public void render(FireballRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (state.tick_count >= 2 || !(state.camera_dist_sqr < (double)MIN_CAMERA_DISTANCE_SQUARED)) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(state.y_rot));
            poseStack.mulPose(Axis.XP.rotationDegrees(state.rendering_rot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(state.x_rot));
            poseStack.translate(0f, -1.0f, 0f);
            VertexConsumer vertex_consumer = ItemRenderer.getFoilBuffer(buffer,
                    this.model.renderType(FireballProjectileModel.TEXTURE_LOCATION), false, false);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
            this.model.renderToBuffer(poseStack, vertex_consumer, packedLight, OverlayTexture.RED_OVERLAY_V);
//            RenderSystem.disableBlend();
            poseStack.popPose();
            super.render(state, poseStack, buffer, packedLight);
        }
    }

    @Override
    public FireballRenderState createRenderState() {
        return new FireballRenderState();
    }
}