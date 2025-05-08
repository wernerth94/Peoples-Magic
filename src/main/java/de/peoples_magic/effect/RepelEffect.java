package de.peoples_magic.effect;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import de.peoples_magic.entity.ModEntities;
import de.peoples_magic.entity.spells.SummonedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class RepelEffect  extends MobEffect {
    protected RepelEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }


    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            int spell_level = Util.level_from_progression(Config.repel_progression, player.getData(ModAttachments.REPEL_SPIDERS_REPELLED.get()));
            double range = (double)Util.get_or_last(Config.repel_range, spell_level);
            Vec3 player_pos = player.position();
            AABB box = new AABB(player_pos.subtract(range, range, range), player_pos.add(range, range, range));
            List<Entity> affected_entities = level.getEntities(null, box);
            List<LivingEntity> cleaned_list = new ArrayList<>();
            for (Entity ent : affected_entities) {
                if (ent instanceof LivingEntity living_entity) {
                    if (!ent.equals(player)) {
                        cleaned_list.add(living_entity);
                    }
                }
            }
            for (LivingEntity ent : cleaned_list) {
                int knowledge = player.getData(ModAttachments.REPEL_KNOWLEDGE.get());
                if (knowledge == 2) {
                    ent.addEffect(new MobEffectInstance(ModEffects.APPLY_STASIS_EFFECT, 20, 1,
                            false, false, false));
                } else if (knowledge == 3 && ent instanceof Monster && !(ent instanceof SummonedEntity)) {
                    if (RandomGenerator.getDefault().nextDouble() < 0.12 / cleaned_list.size()) {
                        ModEntities.CUSTOM_LIGHTNING.get().spawn(level, ent.getOnPos(), EntitySpawnReason.TRIGGERED);
                    }
                } else {
                    Vec3 ent_pos = ent.position();
                    Vec3 direction = ent_pos.subtract(player_pos);
                    direction = direction.normalize();
                    ent.move(MoverType.PLAYER, direction);
                    if (ent instanceof Spider || ent instanceof CaveSpider) {
                        float duration = Util.get_or_last(Config.repel_durations, spell_level);
                        long next_pull = player.getData(ModAttachments.REPEL_LAST_REPEL.get()) + ((long) Math.floor(duration) * 1000L);
                        if (System.currentTimeMillis() > next_pull) {
                            if (!ent.onGround()) {
                                SpellUtil.increase_repel_spiders(player, 1);
                                player.setData(ModAttachments.REPEL_LAST_REPEL.get(), System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        }
        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
