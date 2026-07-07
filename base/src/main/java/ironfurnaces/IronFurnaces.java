package ironfurnaces;

import ironfurnaces.init.Reference;
import ironfurnaces.network.FurnaceSettingsPayload;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class IronFurnaces implements ModInitializer {

    @Override
    public void onInitialize() {
        Reference.init();

        PayloadTypeRegistry.serverboundPlay().register(FurnaceSettingsPayload.TYPE, FurnaceSettingsPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(FurnaceSettingsPayload.TYPE, (payload, context) -> {
            ((net.minecraft.server.level.ServerLevel) context.player().level()).getServer().execute(() -> {
                BlockPos pos = payload.pos();
                int index = payload.index();
                int set = payload.set();
                BlockEntity tee = context.player().level().getBlockEntity(pos);
                if (tee != null && tee instanceof BlockIronFurnaceTileBase) {
                    BlockIronFurnaceTileBase te = (BlockIronFurnaceTileBase) tee;

                    te.furnaceSettings.set(index, set);
                    te.getLevel().sendBlockUpdated(pos, te.getLevel().getBlockState(pos), te.getLevel().getBlockState(pos), 2);
                }
            });
        });
    }
}
