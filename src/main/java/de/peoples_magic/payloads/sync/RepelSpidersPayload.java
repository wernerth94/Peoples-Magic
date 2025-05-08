package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RepelSpidersPayload(int amount)  implements CustomPacketPayload {
    public static final Type<RepelSpidersPayload> TYPE = new Type<>(Util.rec_loc("repel_spiders_payload"));

    public static final StreamCodec<ByteBuf, RepelSpidersPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            RepelSpidersPayload::amount,
            RepelSpidersPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
