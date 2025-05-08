package de.peoples_magic.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.peoples_magic.Util;
import de.peoples_magic.entity.spells.AetherGripProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class AetherGripProjectileModel extends EntityModel<AetherGripRenderState> {
	public static final ResourceLocation TEXTURE_LOCATION = Util.rec_loc("textures/entity/projectiles/aether_grip.png");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(TEXTURE_LOCATION, "main");

	public AetherGripProjectileModel(ModelPart root) {
		super(root.getChild("bb_main"));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.7718F, 19.4785F, -1.0337F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 22).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4782F, -2.9785F, 4.0337F, 0.3074F, -0.0816F, -0.2789F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6725F, -2.9428F, 5.9589F, -0.213F, -0.077F, -0.2588F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7282F, -3.4785F, 4.0337F, 0.3074F, -0.0816F, -0.1044F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9225F, -3.4428F, 5.9589F, -0.213F, -0.077F, -0.0843F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8275F, -3.4428F, 5.9589F, -0.213F, -0.077F, 0.0466F));

		PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0218F, -3.4785F, 4.0337F, 0.3074F, -0.0816F, 0.0265F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 13).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2718F, -1.7285F, -0.2163F, 0.4785F, -0.0536F, 0.0381F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.3275F, -3.1928F, 5.4589F, -0.1838F, 0.1239F, 0.194F));

		PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0218F, -3.2285F, 4.0337F, 0.3398F, 0.1239F, 0.194F));

		PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 13).addBox(-0.5F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0218F, -1.4785F, -0.2163F, 0.5265F, 0.037F, 0.3459F));

		PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(16, 7).addBox(-1.5F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2282F, -1.7285F, -0.2163F, 0.5168F, -0.078F, -0.07F));

		PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 25).addBox(-1.5F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7282F, 3.5215F, 0.5337F, 2.9974F, 0.1447F, -1.5911F));

		PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(16, 7).addBox(-1.5F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4782F, 4.5215F, -3.7163F, -2.6353F, 0.2742F, -1.6106F));

		PartDefinition cube_r14 = bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(16, 7).addBox(-1.5F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7282F, -1.4785F, -0.2163F, 0.5578F, -0.0828F, -0.2269F));

		PartDefinition cube_r15 = bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -0.5F, -1.6887F, 2.0F, 1.0F, 5.6887F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9782F, 2.9231F, -4.6551F, 0.9375F, 0.2489F, -0.1789F));

		PartDefinition cube_r16 = bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(24, 3).addBox(-1.0F, -0.5F, -1.1556F, 2.0F, 1.0F, 2.3113F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7282F, 5.0031F, -5.7865F, 1.3233F, 0.2389F, -0.328F));

		PartDefinition cube_r17 = bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.5728F, 0.0F, 6.0F, 1.0F, 5.8993F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7718F, 4.8443F, -4.8657F, 0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r18 = bb_main.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 19).addBox(-3.0F, -1.5728F, -2.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7718F, 4.3443F, -4.6157F, 1.3526F, 0.0F, 0.0F));

		PartDefinition cube_r19 = bb_main.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, -0.5F, -1.6887F, 2.0F, 1.0F, 5.6887F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5218F, 2.9231F, -4.6551F, 0.9304F, -0.284F, 0.2058F));

		PartDefinition cube_r20 = bb_main.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -0.5F, -1.1556F, 2.0F, 1.0F, 2.3113F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2718F, 5.2531F, -5.7865F, 1.2658F, -0.2287F, 0.5196F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public void setupAnim(AetherGripRenderState state) {
		super.setupAnim(state);
	}

}