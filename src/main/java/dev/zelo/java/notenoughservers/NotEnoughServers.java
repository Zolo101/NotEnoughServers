package dev.zelo.java.notenoughservers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.zelo.java.notenoughservers.mixins.ServerListAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
//import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.ServerList;
//import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
//import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class NotEnoughServers implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-servers";
//    public static KeyBinding MS_TOP = KeyBindingHelper.registerKeyBinding(new KeyBinding("not-enough-servers.MS_TOP", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME, "not-enough-servers.name"));
//    public static KeyBinding MS_BOTTOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("not-enough-servers.MS_BOTTOM", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_END, "not-enough-servers.name"));
//    public static KeyBinding MS_DELETE = KeyBindingHelper.registerKeyBinding(new KeyBinding("not-enough-servers.MS_DELETE", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DELETE, "not-enough-servers.name"));
//    public static KeyBinding MS_ADD = KeyBindingHelper.registerKeyBinding(new KeyBinding("not-enough-servers.MS_ADD", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_A, "not-enough-servers.name"));
//    public static KeyBinding MS_RENAME = KeyBindingHelper.registerKeyBinding(new KeyBinding("not-enough-servers.MS_RENAME", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "not-enough-servers.name"));


    public int attemptAdd(com.mojang.brigadier.context.CommandContext<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        String serverIP = StringArgumentType.getString(context, "ip");
        String serverName = StringArgumentType.getString(context, "name");
        String addressRaw = client.getNetworkHandler().getConnection().getAddress().toString();
        String[] addresses = addressRaw.toLowerCase().split("/");
        ServerList serverList = new ServerList(client);
        boolean serverIPThis = serverIP.equalsIgnoreCase("this");

        serverList.loadFile();
        if (serverIPThis) {
            if (client.isInSingleplayer()) {
                // TODO: Do translation key support
                Text failText = Text.literal("NES: 'this' only works when in a server!")
                        .styled((style -> style.withColor(Formatting.RED)));
                context.getSource().sendFeedback(failText);
                return 0;
            } else {
                ServerInfo info = new ServerInfo(serverName, addresses[0], false);
                serverList.add(info, false);
            }
        } else {
            ServerInfo info = new ServerInfo(serverName, serverIP, false);
            serverList.add(info, false);
        }
        serverList.saveFile();

        String addressToShow = serverIPThis ? addresses[0] : serverIP;
        // TODO: Do translation key support
        Text successText = Text.literal("NES: Successfully added '" + serverName + "' (" + addressToShow + ")")
                .styled((style -> style.withColor(Formatting.GREEN)));
        context.getSource().sendFeedback(successText);
        return 0;
    }

    public int attemptJoin(com.mojang.brigadier.context.CommandContext<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        String serverName = StringArgumentType.getString(context, "name");
        ServerList serverList = new ServerList(client);
        serverList.loadFile();
        List<ServerInfo> servers = ((ServerListAccessor) serverList).getServers();
        Optional<ServerInfo> serverToJoin = servers.stream()
                .filter(serverInfo -> serverInfo.name.equals(serverName) || serverInfo.address.equals(serverName))
                .findFirst();

        if (serverToJoin.isPresent()) {
            // this does not seem to help the crashing in singleplayer
            client.world.disconnect();
            if (!client.isInSingleplayer()) {
                client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
            } else {
                client.disconnect();
            }
            client.send(() -> ConnectScreen.connect(client.currentScreen, client, ServerAddress.parse(serverToJoin.get().address), serverToJoin.get()));
        } else {
            // TODO: Do translation key support
            Text errorText = Text.literal("Server name/address is not in the serverlist!")
                    .styled((style -> style.withColor(Formatting.RED)));
            context.getSource().sendFeedback(errorText);
        }
        return 0;
    }

    @Override
    public void onInitializeClient() {
        // TODO: Do keybind and translation string support
        SuggestionProvider<FabricClientCommandSource> addActions = (context, builder) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (!client.isInSingleplayer()) builder.suggest("this");

            return CompletableFuture.completedFuture(builder.build());
        };

        SuggestionProvider<FabricClientCommandSource> joinActions = (context, builder) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ServerList serverList = new ServerList(client);
            serverList.loadFile();
            List<ServerInfo> servers = ((ServerListAccessor) serverList).getServers();

            for (ServerInfo server : servers) {
                // default to IP for nameless servers
                String name = (server.name.isEmpty()) ? server.address : server.name;
                builder.suggest(name);
            }

            return CompletableFuture.completedFuture(builder.build());
        };

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(literal("addserver")
                    .then(argument("ip", StringArgumentType.string())
                            .suggests(addActions)
                            .then(argument("name", StringArgumentType.greedyString())
                                    .executes(this::attemptAdd)))));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(literal("joinserver")
                .then(argument("name", StringArgumentType.greedyString())
                        .suggests(joinActions)
                        .executes(this::attemptJoin))));
    }
}
