package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record DisplayMessagePayload(String msg, int time)  implements CustomPacketPayload {
    public static final Type<DisplayMessagePayload> TYPE = new Type<>(Util.rec_loc("display_message_payload"));

    public static final StreamCodec<ByteBuf, DisplayMessagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            DisplayMessagePayload::msg,
            ByteBufCodecs.INT,
            DisplayMessagePayload::time,
            DisplayMessagePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
