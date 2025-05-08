package de.peoples_magic.payloads.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CastRepelPayload(int dummy) implements CustomPacketPayload {

    public static final Type<CastRepelPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("peoples_magic", "spell_repel_payload"));

    public static final StreamCodec<ByteBuf, CastRepelPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CastRepelPayload::dummy,
            CastRepelPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
