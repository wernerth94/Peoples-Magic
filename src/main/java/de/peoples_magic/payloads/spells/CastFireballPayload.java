package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastFireballPayload(int level) implements CustomPacketPayload {

    public static final Type<CastFireballPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "spell_fireball_payload"));

    public static final StreamCodec<ByteBuf, CastFireballPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastFireballPayload::level,
            CastFireballPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
