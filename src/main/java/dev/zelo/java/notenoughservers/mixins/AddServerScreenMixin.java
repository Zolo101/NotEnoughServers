package dev.zelo.java.notenoughservers.mixins;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {
    public AddServerScreenMixin(Text title) {
        super(title);
    }

    @Shadow private TextFieldWidget serverNameField;

    // These mixins fix https://bugs.mojang.com/browse/MC-151412
    @ModifyArg(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setTextFieldFocused(Z)V"), index = 0)
    protected boolean inject(boolean focused) {
        return false;
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    protected void init_end(CallbackInfo ci) {
        this.setInitialFocus(serverNameField);
    }
}
