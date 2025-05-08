package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastSummonAllyPayload(int dummy) implements CustomPacketPayload {

    public static final Type<CastSummonAllyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "cast_summon_ally_payload"));

    public static final StreamCodec<ByteBuf, CastSummonAllyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastSummonAllyPayload::dummy,
            CastSummonAllyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
