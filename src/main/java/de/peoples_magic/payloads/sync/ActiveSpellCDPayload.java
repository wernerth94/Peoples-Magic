package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ActiveSpellCDPayload(String spell_name)  implements CustomPacketPayload {
    public static final Type<ActiveSpellCDPayload> TYPE = new Type<>(Util.rec_loc("active_spell_cd_payload"));

    public static final StreamCodec<ByteBuf, ActiveSpellCDPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ActiveSpellCDPayload::spell_name,
            ActiveSpellCDPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
