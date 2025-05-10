package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NoManaPayload(boolean dummy)  implements CustomPacketPayload {
    public static final Type<NoManaPayload> TYPE = new Type<>(Util.rec_loc("no_mana_payload"));

    public static final StreamCodec<ByteBuf, NoManaPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            NoManaPayload::dummy,
            NoManaPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
