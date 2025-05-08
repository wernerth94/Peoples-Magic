package de.peoples_magic.entity.client;

import net.minecraft.client.renderer.entity.state.CreakingRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForestGuardianRenderState extends CreakingRenderState {

    public int tick_count;
    public double camera_dist_sqr;
    public float y_rot;
    public float x_rot;
    public float rendering_rot;


    public ForestGuardianRenderState() {
    }
}
