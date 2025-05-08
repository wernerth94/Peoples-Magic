package de.peoples_magic.entity.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazenKnightRenderState extends SkeletonRenderState {

    public boolean is_passenger;

    public BlazenKnightRenderState() {
    }
}
