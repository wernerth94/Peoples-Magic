package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateManaPayload(double mana)  implements CustomPacketPayload {
    public static final Type<UpdateManaPayload> TYPE = new Type<>(Util.rec_loc("update_mana_payload"));

    public static final StreamCodec<ByteBuf, UpdateManaPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            UpdateManaPayload::mana,
            UpdateManaPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
