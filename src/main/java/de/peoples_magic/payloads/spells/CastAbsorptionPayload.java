package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastAbsorptionPayload(int level) implements CustomPacketPayload {

    public static final Type<CastAbsorptionPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "spell_absorption_payload"));

    public static final StreamCodec<ByteBuf, CastAbsorptionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastAbsorptionPayload::level,
            CastAbsorptionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
