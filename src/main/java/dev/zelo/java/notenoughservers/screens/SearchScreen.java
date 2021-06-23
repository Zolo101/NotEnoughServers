package dev.zelo.java.notenoughservers.screens;

import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.TranslatableText;

public class SearchScreen extends Screen {
    protected MultiplayerServerListWidget serverListWidget;
    private final Screen parent;
    private ButtonWidget buttonJoin;
    private ServerInfo selectedEntry;
    protected TextFieldWidget searchBox;

    public SearchScreen(Screen parent) {
        super(new TranslatableText("multiplayersearch.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        // back button
        this.addButton(new ButtonWidget(this.width / 2 + 4 + 76, this.height - 28, 75, 20, ScreenTexts.CANCEL, (buttonWidget) -> {
            this.client.openScreen(this.parent);
        }));
        this.buttonJoin = (ButtonWidget)this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 52, 100, 20, new TranslatableText("selectServer.select"), (buttonWidget) -> {
            this.client.openScreen(new ConnectScreen(this, this.client, selectedEntry));
        }));

        // search bar
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, new TranslatableText("selectWorld.search"));

        // serverlist
//        this.serverListWidget = new MultiplayerServerListWidget(this, this.client, this.width, this.height, 32, this.height - 64, 36)
    }
}
