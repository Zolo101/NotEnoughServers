package dev.zelo.java.notenoughservers.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.options.ServerList;
import net.minecraft.text.Text;
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
            case 268: // HOME key
                MultiplayerServerListWidget.Entry topEntry = serverListWidget.children().get(0);
                serverListWidget.setSelected(topEntry);
                serverListWidget.setScrollAmount(-serverListWidget.getMaxScroll());
                break;
            case 269: // END key
                MultiplayerServerListWidget.Entry bottomEntry = serverListWidget.children().get(serverListWidget.children().size()-2);
                serverListWidget.setSelected(bottomEntry);
                serverListWidget.setScrollAmount(serverListWidget.getMaxScroll());
                break;
            case 261: // DELETE key
                if (Screen.hasShiftDown() && serverListWidget.getSelected() != null) {
                    serverList.remove(((MultiplayerServerListWidget.ServerEntry) serverListWidget.getSelected()).getServer());
                    serverList.saveFile();
                    serverListWidget.setSelected(null);
                    serverListWidget.setServers(this.serverList);
                }
                break;
            default:
                break;
        }
    }
}
