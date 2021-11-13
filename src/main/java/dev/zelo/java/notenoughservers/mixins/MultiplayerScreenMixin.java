package dev.zelo.java.notenoughservers.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Shadow protected MultiplayerServerListWidget serverListWidget;

    @Shadow private ServerList serverList;

    @Shadow public abstract ServerList getServerList();

    @Shadow protected abstract void removeEntry(boolean confirmedAction);

    @Shadow protected abstract void editEntry(boolean confirmedAction);

    @Shadow private ButtonWidget buttonEdit;

    @Shadow private ButtonWidget buttonDelete;

    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
//        this.addButton(new ButtonWidget(this.width / 2 + 4 + 50, this.height - 320, 100, 20, new TranslatableText("selectServer.search"), (buttonWidget) -> {
//            this.client.openScreen(new SearchScreen(this));
//        }));
    }

    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"))
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println(keyCode);
//        System.out.println(scanCode);
//        System.out.println(modifiers);
        // return true if this does something
        if (serverListWidget.children().size() == 0) return;
        switch (keyCode) {
            case GLFW.GLFW_KEY_HOME: // HOME key
                MultiplayerServerListWidget.Entry topEntry = serverListWidget.children().get(0);
                serverListWidget.setSelected(topEntry);
                serverListWidget.setScrollAmount(-serverListWidget.getMaxScroll());
                break;
            case GLFW.GLFW_KEY_END: // END key
                MultiplayerServerListWidget.Entry bottomEntry = serverListWidget.children().get(serverListWidget.children().size()-2);
                serverListWidget.setSelected(bottomEntry);
                serverListWidget.setScrollAmount(serverListWidget.getMaxScroll());
                break;
            case GLFW.GLFW_KEY_DELETE: // DELETE key
                if (serverListWidget.getSelectedOrNull() != null) {
                    if (Screen.hasShiftDown()) { // quick delete
                        this.removeEntry(true);
                    } else { // ask to delete
                        this.buttonDelete.onPress();
                    }
                }
                break;
            case GLFW.GLFW_KEY_R: // R key (Rename)
                if (serverListWidget.getSelectedOrNull() != null) {
                    this.buttonEdit.onPress();
                }
            default:
                break;
        }
    }
}
