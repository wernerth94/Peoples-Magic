package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastHastePayload(int dummy) implements CustomPacketPayload {

    public static final Type<CastHastePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "cast_haste_payload"));

    public static final StreamCodec<ByteBuf, CastHastePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastHastePayload::dummy,
            CastHastePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
