package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AetherGripPullsPayload(int pulls)  implements CustomPacketPayload {
    public static final Type<AetherGripPullsPayload> TYPE = new Type<>(Util.rec_loc("aether_grip_pulls_payload"));

    public static final StreamCodec<ByteBuf, AetherGripPullsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            AetherGripPullsPayload::pulls,
            AetherGripPullsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
