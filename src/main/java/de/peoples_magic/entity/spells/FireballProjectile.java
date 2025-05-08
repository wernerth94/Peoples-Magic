package de.peoples_magic.entity.spells;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FireballProjectile extends AbstractArrow {

    private float rotation;
    private final int remove_after;
    private final int spell_level;

    public FireballProjectile(EntityType<? extends FireballProjectile> entityType, Level level) {
        super(entityType, level);
        this.remove_after = 20 * 10;
        this.spell_level = -1;
    }

    public FireballProjectile(Player player, Level level,
                              int spell_level, int remove_after) {
        super(ModEntities.FIREBALL_PROJECTILE.get(), player.getX(), player.getEyeY() - 0.5f, player.getZ(),
                level, new ItemStack(Items.SALMON), null);
        this.remove_after = remove_after;
        this.spell_level = spell_level;
        this.setOwner(player);
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        super.shootFromRotation(shooter, x, y, z, velocity, inaccuracy);
        float vel = Util.get_or_last(Config.fireball_velocities, spell_level);
        this.setDeltaMovement(this.getDeltaMovement().scale(vel));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level() instanceof ServerLevel server_level) {
            Player player = (Player) this.getOwner();
            Entity entity = result.getEntity();
            float damage = Util.get_or_last(Config.fireball_damages, spell_level);
            if (this.getOwner() != null && this.getOwner().getData(ModAttachments.FIREBALL_KNOWLEDGE.get()) == 3) {
                damage *= 2;
            }
            if (player != null) {
                player.setLastHurtMob(entity);
                SpellUtil.increase_fireball_hit((ServerPlayer) player, 1);
            }
            DamageSource damagesource = this.damageSources().magic();
            if (entity.hurtServer(server_level, damagesource, damage) && entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity)entity;
                EnchantmentHelper.doPostAttackEffects((ServerLevel)this.level(), target, damagesource);
            }

            Vec3i vec3i = entity.getDirection().getUnitVec3i(); // Normal vector
            Vec3 vec3 = Vec3.atLowerCornerOf(vec3i).multiply((double)0.25F, (double)0.25F, (double)0.25F);
            Vec3 vec31 = entity.getPosition(5).add(vec3);
            this.explode(vec31);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide) {
            Vec3i vec3i = result.getDirection().getUnitVec3i();
            Vec3 vec3 = Vec3.atLowerCornerOf(vec3i).multiply(0.25F, 0.25F, 0.25F);
            Vec3 vec31 = result.getLocation().add(vec3);
            this.explode(vec31);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel server_level) {
            if (this.tickCount > this.remove_after) {
                this.discard();
            }
            if (this.isInWater()) {
                this.discard();
                server_level.playSound(null, this.position().x, this.position().y, this.position().z,
                        SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f);
            }
        }
        else {
            level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Override
    protected void applyGravity() {
        // No Gravity
    }


    protected void explode(Vec3 pos) {
        if (this.getOwner() != null && this.getOwner().getData(ModAttachments.FIREBALL_KNOWLEDGE.get()) == 2) {
            ServerLevel server_level = (ServerLevel)this.level();
            List<Monster> affected_entities = server_level.getEntitiesOfClass(Monster.class, new AABB(pos, pos).inflate(4.0D));
            for (int i = affected_entities.size() - 1; i >= 0; i--) {
                if (affected_entities.get(i) instanceof SummonedEntity) {
                    affected_entities.remove(i);
                }
            }
            SpellUtil.add_active_fireball_void(pos, affected_entities);
            server_level.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.3, 0.3, 0.3, 0);
            server_level.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0F, 1.0F);
            for (LivingEntity entity : affected_entities) {
                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20*3, 2, false, true));
            }
        }
        else {
            float knockback = Util.get_or_last(Config.fireball_knockbacks, spell_level);
            SimpleExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
                    false, true, Optional.of(knockback),
                    BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
            this.level().explode(this, null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(),
                    2.0F, false, Level.ExplosionInteraction.TRIGGER,
                    ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.SOUL_ESCAPE);
            this.level().playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 0.4f, 0.5f);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.SALMON);
    }
}
