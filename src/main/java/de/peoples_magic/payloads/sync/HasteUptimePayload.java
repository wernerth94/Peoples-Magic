package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HasteUptimePayload(float uptime)  implements CustomPacketPayload {
    public static final Type<HasteUptimePayload> TYPE = new Type<>(Util.rec_loc("haste_uptime_payload"));

    public static final StreamCodec<ByteBuf, HasteUptimePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            HasteUptimePayload::uptime,
            HasteUptimePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
