package de.peoples_magic.payloads.sync;

import de.peoples_magic.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FarmingAnimalsBredPayload(int animals)  implements CustomPacketPayload {
    public static final Type<FarmingAnimalsBredPayload> TYPE = new Type<>(Util.rec_loc("farming_animals_bred"));

    public static final StreamCodec<ByteBuf, FarmingAnimalsBredPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FarmingAnimalsBredPayload::animals,
            FarmingAnimalsBredPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
