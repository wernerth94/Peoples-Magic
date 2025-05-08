package de.peoples_magic.entity.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FireballRenderState extends EntityRenderState {

    public int tick_count;
    public double camera_dist_sqr;
    public float y_rot;
    public float x_rot;
    public float rendering_rot;


    public FireballRenderState() {
    }
}
