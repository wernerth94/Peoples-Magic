package de.peoples_magic.block;


import com.mojang.serialization.MapCodec;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import de.peoples_magic.potion.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.data.PackOutput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class ManaPoolBlock extends TransparentBlock implements EntityBlock {
    public static final MapCodec<ManaPoolBlock> CODEC = simpleCodec(ManaPoolBlock::new);
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);

    public static final IntegerProperty MANA_STORED = ModBlockStateProperties.MANA_WELL_STORED_MANA;
    public static final IntegerProperty FILL_LEVEL = ModBlockStateProperties.MANA_WELL_FILL_LEVEL;

    public ManaPoolBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MANA_STORED, Integer.valueOf(0)));
        this.registerDefaultState(this.stateDefinition.any().setValue(FILL_LEVEL, Integer.valueOf(0)));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaPoolBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ModBlockEntities.MANA_POOL_BLOCK_ENTITY.get()) {
            return (lvl, pos, st, be) -> ManaPoolBlockEntity.tick(lvl, pos, st, (ManaPoolBlockEntity) be);
        }
        return EntityBlock.super.getTicker(level, state, blockEntityType);
    }

    @Override
    public @Nullable <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
        return EntityBlock.super.getListener(level, blockEntity);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            refill_players_mana(state, level, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            if (stack.getItem() == Items.GLASS_BOTTLE) {
                try_fill_bottle(stack, state, level, player, pos);
            }
            else if (stack.getItem() == Items.COAL && Config.test_mode) {
                change_stored_mana(state, level, pos, 20);
            } else {
                refill_players_mana(state, level, pos, player);
            }
        }
        return InteractionResult.SUCCESS;
    }

//    @Override
//    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        if (!level.isClientSide()) {
//            change_stored_mana(state, level, pos, Config.mana_well_mana_per_tick);
//        }
//    }


    private void try_fill_bottle(ItemStack stack, BlockState state, Level level, Player player, BlockPos pos) {
        int mana_stored = state.getValue(MANA_STORED);
        if (mana_stored >= 50) {
            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1f, 1.5f);
            stack.shrink(1);
            ItemStack potion = PotionContents.createItemStack(Items.POTION, ModPotions.MANA_POTION);
            player.getInventory().add(potion);
            change_stored_mana(state, level, pos, -50);
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS,
                    1f, 1f);
        }
    }


    public void change_stored_mana(BlockState state, Level level, BlockPos pos, int amount) {
        int mana_stored = state.getValue(MANA_STORED);
        if ((amount > 0 && mana_stored < 100) ||
            (amount < 0 && mana_stored > 0)) {
            mana_stored += amount;
            mana_stored = Math.min(Math.max(mana_stored, 0), 100);
            BlockState blockstate = state.setValue(MANA_STORED, mana_stored);
            blockstate = set_fill_level(blockstate);
            level.setBlockAndUpdate(pos, blockstate);
        }
    }

    private BlockState set_fill_level(BlockState state) {
        int mana_stored = state.getValue(MANA_STORED);

        if (mana_stored == 0) {
            return state.setValue(FILL_LEVEL, 0);
        } else if (mana_stored <= 20) {
            return state.setValue(FILL_LEVEL, 1);
        } else if (mana_stored <= 40) {
            return state.setValue(FILL_LEVEL, 2);
        } else if (mana_stored <= 60) {
            return state.setValue(FILL_LEVEL, 3);
        } else if (mana_stored <= 80) {
            return state.setValue(FILL_LEVEL, 4);
        } else {
            return state.setValue(FILL_LEVEL, 5);
        }
    }


    private void refill_players_mana(BlockState state, Level level, BlockPos pos, Player player) {
        int mana_stored = state.getValue(MANA_STORED);
        double current_player_mana = player.getData(ModAttachments.PLAYER_MANA);
        double usable_mana_in_well = Math.floor(mana_stored);
        double transferable_amount = Math.min(player.getAttributeValue(ModAttributes.MAX_MANA) - current_player_mana, usable_mana_in_well);
        if (transferable_amount > 0) {
            Util.update_player_mana(player, current_player_mana + transferable_amount);
            change_stored_mana(state, level, pos, (int) -transferable_amount);
            level.playSound(
                    null,
                    pos,
                    SoundEvents.BOAT_PADDLE_WATER,
                    SoundSource.BLOCKS,
                    0.8F,
                    2.0F
            );
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MANA_STORED);
        builder.add(FILL_LEVEL);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }



    @Override
    public MapCodec<ManaPoolBlock> codec() {
        return CODEC;
    }

}
