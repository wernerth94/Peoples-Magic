package de.peoples_magic.block;

import de.peoples_magic.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.random.RandomGenerator;

public class ManaPoolBlockEntity extends BlockEntity {
    public ManaPoolBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MANA_POOL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaPoolBlockEntity block_entity) {
        if (!level.isClientSide) {
            if (RandomGenerator.getDefault().nextDouble() < (Config.mana_well_mana_per_second / 20f)) {
                ManaPoolBlock block = (ManaPoolBlock) state.getBlock();
                block.change_stored_mana(state, level, pos, 1);
                if (RandomGenerator.getDefault().nextInt(3) == 0) {
                    level.playSound(
                            null,
                            pos,
                            SoundEvents.BOAT_PADDLE_WATER,
                            SoundSource.BLOCKS,
                            0.5F,
                            2.0F
                    );
                }
            }
        }
        else {
            if (RandomGenerator.getDefault().nextInt(90) == 0) {
                level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            }
        }
    }
}
