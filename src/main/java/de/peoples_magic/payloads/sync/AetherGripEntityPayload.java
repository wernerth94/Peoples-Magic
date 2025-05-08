package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AetherGripEntityPayload(int id)  implements CustomPacketPayload {
    public static final Type<AetherGripEntityPayload> TYPE = new Type<>(Util.rec_loc("aether_grip_entity_payload"));

    public static final StreamCodec<ByteBuf, AetherGripEntityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            AetherGripEntityPayload::id,
            AetherGripEntityPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
