package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SummonAllySummonsPayload(int summons)  implements CustomPacketPayload {
    public static final Type<SummonAllySummonsPayload> TYPE = new Type<>(Util.rec_loc("summon_ally_summons_payload"));

    public static final StreamCodec<ByteBuf, SummonAllySummonsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SummonAllySummonsPayload::summons,
            SummonAllySummonsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
