package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateKnowledgePayload(String spell, int knowledge)  implements CustomPacketPayload {
    public static final Type<UpdateKnowledgePayload> TYPE = new Type<>(Util.rec_loc("update_knowledge_payload"));

    public static final StreamCodec<ByteBuf, UpdateKnowledgePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            UpdateKnowledgePayload::spell,
            ByteBufCodecs.VAR_INT,
            UpdateKnowledgePayload::knowledge,
            UpdateKnowledgePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
