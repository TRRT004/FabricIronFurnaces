package ironfurnaces;

import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.lwjgl.glfw.GLFW;

public class IronFurnacesClient implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        Reference.initClient();



    }

    public static boolean isShiftKeyDown() {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
    public static boolean isKeyDown(int glfw) {
        com.mojang.blaze3d.platform.InputConstants.Key key = InputConstants.Type.KEYSYM.createFromCode(glfw);
        int keyCode = key.getCode();
        if (keyCode != InputUtil.UNKNOWN_KEY.getCode()) {
            long windowHandle = Minecraft.getInstance().getWindow().getHandle();
            try {
                if (key.getCategory() == InputConstants.Type.KEYSYM) {
                    return InputConstants.isKeyDown(windowHandle, keyCode);
                } /**else if (key.getType() == InputMappings.Type.MOUSE) {
                 return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                 }**/
            } catch (Exception ignored) {
            }
        }
        return false;
    }



}
