package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateCooldownsPayload(
        String name, float cd)
//        float absorption, float fireball, float ice_cone, float aether_grip, float haste, float farming, float summon_ally)
        implements CustomPacketPayload {
    public static final Type<UpdateCooldownsPayload> TYPE = new Type<>(Util.rec_loc("update_cd_payload"));

    public static final StreamCodec<ByteBuf, UpdateCooldownsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            UpdateCooldownsPayload::name,
            ByteBufCodecs.FLOAT,
            UpdateCooldownsPayload::cd,
            UpdateCooldownsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
