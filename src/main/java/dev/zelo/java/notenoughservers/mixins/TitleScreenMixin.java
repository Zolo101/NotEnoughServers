package dev.zelo.java.notenoughservers.mixins;

/*
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    public TitleScreenMixin(Text title) {
        super(title);
    }
//
//    @Redirect(method = "initWidgetsNormal(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;addButton(LButton)LButtonWidget"))
//    private Screen ultiScreen() {
//        return new ServerlistUltimateScreen(this);
//    }
//
//    @Shadow protected abstract void initWidgetsNormal(int y, int spacingY);
    @Inject(method = "initWidgetsNormal(II)V", at = @At("TAIL"))
    private void inject(int y, int spacingY, CallbackInfo ci) {
        boolean bl = this.client.isMultiplayerEnabled();
        ButtonWidget.TooltipSupplier tooltipSupplier = bl ? ButtonWidget.EMPTY : (buttonWidget, matrixStack, i, j) -> {
            if (!buttonWidget.active) {
                this.renderOrderedTooltip(matrixStack, this.client.textRenderer.wrapLines(new TranslatableText("title.multiplayer.disabled"), Math.max(this.width / 2 - 43, 170)), i, j);
            }

        };
        this.addButton(new ButtonWidget(this.width / 2 - 300, y + spacingY * 1, 200, 20, new TranslatableText("menu.multiplayer.ultimate"), (buttonWidget) -> {
            Screen screen = new ServerlistUltimateScreen(this);
            this.client.openScreen((Screen) screen);
        }, tooltipSupplier));
    }
}
 */