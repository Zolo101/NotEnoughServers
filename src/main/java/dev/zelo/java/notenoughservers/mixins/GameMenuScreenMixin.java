package dev.zelo.java.notenoughservers.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {
    public GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!MinecraftClient.getInstance().isInSingleplayer()) {
            String address = MinecraftClient.getInstance().getNetworkHandler().getConnection().getAddress().toString().toLowerCase();

            // Remove default port (if its there)
            if (address.substring(address.length() - 6).equals(":25565")) {
                address = address.substring(0, address.length() - 6);
            }
            DrawableHelper.drawStringWithShadow(
                matrices,
                this.textRenderer,
                address,
                2,
                this.height - 10,
                16777215
            );
        }
    }
}
