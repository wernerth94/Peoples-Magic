package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AbsorptionMitigationPayload(float mitigation)  implements CustomPacketPayload {
    public static final Type<AbsorptionMitigationPayload> TYPE = new Type<>(Util.rec_loc("absorption_mitigation_payload"));

    public static final StreamCodec<ByteBuf, AbsorptionMitigationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            AbsorptionMitigationPayload::mitigation,
            AbsorptionMitigationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
