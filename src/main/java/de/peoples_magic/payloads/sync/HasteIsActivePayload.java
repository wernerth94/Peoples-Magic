package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HasteIsActivePayload(boolean is_active)  implements CustomPacketPayload {
    public static final Type<HasteIsActivePayload> TYPE = new Type<>(Util.rec_loc("haste_is_active_payload"));

    public static final StreamCodec<ByteBuf, HasteIsActivePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            HasteIsActivePayload::is_active,
            HasteIsActivePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
