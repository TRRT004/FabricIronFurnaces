package ironfurnaces.network;

import ironfurnaces.init.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record FurnaceSettingsPayload(BlockPos pos, int index, int set) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<FurnaceSettingsPayload> TYPE = new CustomPacketPayload.Type<>(
			Identifier.fromNamespaceAndPath(Reference.MOD_ID, "furnace_settings"));

	public static final StreamCodec<RegistryFriendlyByteBuf, FurnaceSettingsPayload> CODEC = StreamCodec
			.of((buf, value) -> {
				buf.writeBlockPos(value.pos);
				buf.writeInt(value.index);
				buf.writeInt(value.set);
			}, buf -> new FurnaceSettingsPayload(buf.readBlockPos(), buf.readInt(), buf.readInt()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
