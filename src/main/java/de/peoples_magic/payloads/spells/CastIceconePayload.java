package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastIceconePayload(int level) implements CustomPacketPayload {

    public static final Type<CastIceconePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "spell_icecone_payload"));

    public static final StreamCodec<ByteBuf, CastIceconePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastIceconePayload::level,
            CastIceconePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
