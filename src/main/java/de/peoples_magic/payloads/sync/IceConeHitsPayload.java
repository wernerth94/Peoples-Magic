package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record IceConeHitsPayload(int multiple_hit)  implements CustomPacketPayload {
    public static final Type<IceConeHitsPayload> TYPE = new Type<>(Util.rec_loc("ice_cone_multiple_hit_payload"));

    public static final StreamCodec<ByteBuf, IceConeHitsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            IceConeHitsPayload::multiple_hit,
            IceConeHitsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
