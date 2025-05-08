package de.peoples_magic.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.peoples_magic.Util;
import de.peoples_magic.entity.spells.FireballProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class FireballProjectileModel extends EntityModel<FireballRenderState> {
	public static final ResourceLocation TEXTURE_LOCATION = Util.rec_loc("textures/entity/projectiles/texturefireball.png");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(TEXTURE_LOCATION, "main");

	public FireballProjectileModel(ModelPart root) {
		super(root.getChild("bb_main"));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 23).addBox(3.25F, -5.35F, -3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(8, 23).addBox(-4.75F, -5.1F, -3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(20, 28).addBox(2.0F, -12.5F, -3.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(0.0F, -4.0F, -3.75F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 25).addBox(-2.0F, -13.0F, -3.0F, 1.5F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 8).addBox(4.0F, -5.35F, 0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(8, 23).addBox(-4.75F, -5.5F, 0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 10).addBox(2.5F, -3.5F, 0.5F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 31).addBox(-4.0F, -11.75F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 13).addBox(-4.25F, -11.75F, -3.5F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 13).addBox(-4.5F, -11.5F, 2.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 13).addBox(-4.0F, -11.5F, -2.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(24, 20).addBox(3.5F, -10.75F, -3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 16).addBox(3.5F, -12.0F, 0.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 19).addBox(4.0F, -11.25F, 2.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 3).addBox(-5.5F, -7.75F, 0.0F, 1.0F, 2.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-5.0F, -7.75F, -3.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(8, 13).addBox(3.5F, -7.75F, -3.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 0).addBox(4.0F, -8.0F, 0.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 28).addBox(-2.0F, -13.75F, -1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(28, 28).addBox(-1.75F, -12.75F, 1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(20, 28).addBox(2.0F, -12.5F, 1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(2.0F, -12.5F, -1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(32, 25).addBox(0.25F, -13.0F, 2.0F, 1.5F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 28).addBox(0.5F, -13.0F, -3.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(32, 25).addBox(0.25F, -13.5F, 0.0F, 1.5F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 28).addBox(-3.5F, -12.5F, -3.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(8, 30).addBox(-3.25F, -11.75F, -1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(28, 28).addBox(-3.5F, -12.5F, 1.0F, 1.5F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(4.0F, -10.0F, -3.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 5).addBox(3.5F, -10.0F, 0.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(8, 18).addBox(-5.25F, -10.0F, 0.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 20).addBox(-4.5F, -10.0F, -3.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 3).addBox(-5.0F, -7.75F, 2.0F, 1.0F, 2.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-3.0F, -4.0F, -3.75F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 10).addBox(-1.0F, -3.25F, 0.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(10, 27).addBox(0.0F, -4.0F, 2.25F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(10, 27).addBox(-3.0F, -4.0F, 2.25F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(16, 31).addBox(-2.0F, -3.0F, -2.0F, 2.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(16, 31).addBox(1.5F, -3.5F, -1.75F, 2.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 22).addBox(0.0F, -3.25F, -2.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(24, 31).addBox(0.0F, -3.25F, 0.5F, 2.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(32, 22).addBox(-3.0F, -3.25F, -2.0F, 1.0F, 1.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(24, 31).addBox(-3.0F, -3.25F, 0.5F, 2.0F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 5).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -8.0F, 3.75F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 5).addBox(-1.0F, -2.0F, 1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(24, 12).addBox(-1.0F, 2.4F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.0F, 4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 15).addBox(-1.0F, -2.0F, 1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-1.0F, 2.65F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.0F, -5.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -8.0F, -4.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 5).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -8.0F, 4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 5).addBox(-1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.0F, 3.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -4.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(8, 8).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-1.0F, 2.4F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.75F, -4.75F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 7).addBox(-1.0F, -3.0F, -0.25F, 1.0F, 1.0F, 1.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0F, -4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.75F, 3.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, -3.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -8.25F, 3.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.25F, 3.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -8.25F, -4.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r14 = bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(32, 7).addBox(-1.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -8.5F, -4.25F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r15 = bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(24, 4).addBox(-1.0F, 2.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.35F, 3.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(FireballRenderState state) {
		super.setupAnim(state);
	}
}