package de.peoples_magic.entity.spells;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class AetherGripProjectile extends Entity {
    public float MAX_DISTANCE = 14f;
    public Vec3 reversal_point;
    private int reverse_in = -1;
    public boolean already_reversed = false;
    private Player owner;
    private int spell_level;
    private float speed;
    private int knowledge;

    public AetherGripProjectile(EntityType<AetherGripProjectile> aetherGripProjectileEntityType, Level level) {
        super(aetherGripProjectileEntityType, level);
        this.setPos(0, 0, 0);
        reversal_point = new Vec3(0, 0, 0);
    }

    public void as_projectile(Player owner, int spell_level, double x, double y, double z) {
        this.spell_level = spell_level;
        this.owner = owner;
        this.knowledge = this.owner.getData(ModAttachments.AETHER_GRIP_KNOWLEDGE.get());
        this.speed = Util.get_or_last(Config.aether_grip_speed, spell_level);
        this.speed = this.knowledge == 3 ? this.speed / 3f : this.speed;
        this.setPos(x, y, z);
        if (!owner.level().isClientSide) {
            SpellUtil.set_aether_grip_entity((ServerPlayer) owner, this);
        }
    }


    public void shootFromRotation(float x, float y, float z) {
        float dx = -Mth.sin(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        float dy = -Mth.sin((x + z) * (float) (Math.PI / 180.0));
        float dz = Mth.cos(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        Vec3 delta = new Vec3(dx, dy, dz).normalize();
        if (knowledge == 3) {
            MAX_DISTANCE *= 2;
        }
        this.reversal_point = this.position().add(delta.scale(MAX_DISTANCE));

        rotate_in_direction(delta);

        delta = delta.scale(this.speed);
        this.setDeltaMovement(delta);
    }

    public void rotate_in_direction(Vec3 delta) {
        delta = delta.normalize();
        double y_rot = Math.toDegrees(Math.atan2(delta.x, delta.z));
        double pitch = Math.toDegrees(Math.atan2(-delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
        this.setYRot((float)y_rot);
        this.setXRot((float)pitch);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }


    protected void onHitEntity(Entity other) {
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (other instanceof Monster target) {
                if (!(target instanceof SummonedEntity)) {
                    float damage = Util.get_or_last(Config.aether_grip_damages, spell_level);
                    target.hurtServer(serverLevel, this.damageSources().magic(), damage);
                    if (this.already_reversed) {
                        if (knowledge == 2) {
                            Vec3 new_pos = this.position().add(this.getDeltaMovement().scale(1));
                            target.setPos(new_pos.x, new_pos.y, new_pos.z);
                        }
                        else {
                            float strength = Util.get_or_last(Config.aether_grip_pull_strength, spell_level);
                            target.move(MoverType.PLAYER, this.getDeltaMovement().multiply(strength, 0f, strength));
                        }

                        if (target instanceof AbstractSkeleton) {
                            long next_pull_possible = this.owner.getData(ModAttachments.AETHER_GRIP_PULL_CD);
                            if (System.currentTimeMillis() > next_pull_possible) {
                                SpellUtil.increase_aether_grip_pulls((ServerPlayer) this.owner, 1);
                                this.owner.setData(ModAttachments.AETHER_GRIP_PULL_CD, System.currentTimeMillis() + 2000);
                            }
                        }
                    }
                }
            }
        }
    }


    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.owner == null) {
            discard_and_clean();
            return;
        }

        if (!this.level().isClientSide) {
            // reverse based on distance or early trigger by player
            double distance_to_reversal = this.position().subtract(reversal_point).length();
            if (!this.already_reversed && !this.level().isClientSide) {
                if ( distance_to_reversal < 1 ||
                     (this.reverse_in > 0 && this.tickCount > reverse_in) ) {
                    reverse();
                }
            }

            // enter homing mode on way back
            if (this.already_reversed) {
                Vec3 delta_vec3 = this.owner.position().add(0, 1, 0).subtract(this.position());
                delta_vec3 = delta_vec3.normalize().scale(this.speed);
                this.setDeltaMovement(delta_vec3);
                rotate_in_direction(delta_vec3);
            }
            else {
                Vec3 delta_vec3 = this.reversal_point.subtract(this.position()).normalize();
                // slow down when approaching furthest point
                double distance = delta_vec3.length();
                double new_speed = Math.max(0, Math.min(1.0, Math.log10(0.1F * distance + 0.05F) + 1.32F));
                // we need to deepcopy this, or the speed multipliers stack
                delta_vec3 = delta_vec3.scale(new_speed);
                this.setDeltaMovement(delta_vec3);
                rotate_in_direction(delta_vec3);
            }

            // check for entity collisions
            List<Entity> possible_clips = this.level().getEntities(null, this.getBoundingBox());
            for (Entity e : possible_clips) {
                if (Util.do_boxes_intersect(this.getBoundingBox(), e.getBoundingBox()) ) {
                    this.onHitEntity(e);
                }
            }

            // check for block collisions
            if (!this.already_reversed && this.reverse_in < 0) {
                Iterable<VoxelShape> collisions = this.level().getBlockCollisions(this, this.getBoundingBox());
                for (VoxelShape coll : collisions) {
                    this.reverse_in = this.tickCount + 1;
                    break;
                }
            }

            // discard based on time or completed trajectory
            double distance_to_owner = this.owner.position().subtract(this.position()).length();
            if ((this.already_reversed && distance_to_owner < 2f) || this.tickCount > 20*10 ) {
                discard_and_clean();
            }
        }

        Vec3 delta_vec3 = this.getDeltaMovement();
        this.setPos(this.getX() + delta_vec3.x, this.getY() + delta_vec3.y, this.getZ() + delta_vec3.z);
    }

    public void reverse() {
        this.already_reversed = true;
    }

    private void discard_and_clean() {
        if (!this.level().isClientSide) {
            if (this.owner != null) {
                SpellUtil.set_aether_grip_entity((ServerPlayer) owner, null);
            }
            this.discard();
        }
    }


    @Override
    protected void applyGravity() {
        // No Gravity
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        if (damageSource.getEntity() instanceof LivingEntity target) {
            return target != this.owner;
        }
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
