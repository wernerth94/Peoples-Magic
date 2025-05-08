package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FireballHitPayload(int amount)  implements CustomPacketPayload {
    public static final Type<FireballHitPayload> TYPE = new Type<>(Util.rec_loc("fireball_hit_payload"));

    public static final StreamCodec<ByteBuf, FireballHitPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            FireballHitPayload::amount,
            FireballHitPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
