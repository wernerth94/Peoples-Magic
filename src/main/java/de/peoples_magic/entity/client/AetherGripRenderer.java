package de.peoples_magic.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.peoples_magic.entity.spells.AetherGripProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class AetherGripRenderer extends EntityRenderer<AetherGripProjectile, AetherGripRenderState> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(1F);
    private final AetherGripProjectileModel model;

    public AetherGripRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new AetherGripProjectileModel(context.bakeLayer(AetherGripProjectileModel.LAYER_LOCATION));
    }

    @Override
    public AetherGripRenderState createRenderState() {
        return new AetherGripRenderState();
    }


    public @NotNull ResourceLocation getTextureLocation(@Nullable AetherGripProjectile t) {
        return AetherGripProjectileModel.TEXTURE_LOCATION;
    }

    @Override
    public void extractRenderState(AetherGripProjectile entity, AetherGripRenderState state, float partialTick) {
        // Sets the living entity and entity render state info
        super.extractRenderState(entity, state, partialTick);
        // Set our own variables
        state.camera_dist_sqr = this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity);
        state.y_rot = entity.getYRot();
        state.x_rot = entity.getXRot();
    }

    public void render(AetherGripRenderState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!(state.camera_dist_sqr < (double)MIN_CAMERA_DISTANCE_SQUARED)) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(state.y_rot));
            poseStack.mulPose(Axis.XP.rotationDegrees(state.x_rot));

            poseStack.translate(0f, -0.8f, 0f);
            VertexConsumer vertex_consumer = ItemRenderer.getFoilBuffer(buffer,
                    this.model.renderType(AetherGripProjectileModel.TEXTURE_LOCATION), false, false);
            this.model.renderToBuffer(poseStack, vertex_consumer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            super.render(state, poseStack, buffer, packedLight);
        }
    }
}