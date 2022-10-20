package dev.zelo.java.notenoughservers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.zelo.java.notenoughservers.mixins.ServerListAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class NotEnoughServers implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-servers";

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
                .filter(serverInfo -> serverInfo.name.equals(serverName))
                .findFirst();

        if (serverToJoin.isPresent()) {
            // this does not seem to help the crashing in singleplayer
            if (!client.isInSingleplayer()) {
                client.disconnect();
            }
            client.send(() -> ConnectScreen.connect(client.currentScreen, client, ServerAddress.parse(serverToJoin.get().address), serverToJoin.get()));
        } else {
            Text errorText = Text.literal("Unknown server name!")
                    .styled((style -> style.withColor(Formatting.RED)));
            context.getSource().sendFeedback(errorText);
        }
        return 0;
    }

    @Override
    public void onInitializeClient() {
        // TODO: Do keybind and translation string support
//        KeyBinding MS_TOP = KeyBindingHelper.registerKeyBinding(new KeyBinding("Top of List", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME, "Not Enough Servers"));
//        KeyBinding MS_BOTTOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("Bottom of List", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_END, "Not Enough Servers"));
//        KeyBinding MS_DELETE = KeyBindingHelper.registerKeyBinding(new KeyBinding("Delete Server", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DELETE, "Not Enough Servers"));
//        KeyBinding MS_ADD = KeyBindingHelper.registerKeyBinding(new KeyBinding("Add Server", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_A, "Not Enough Servers"));
//        KeyBinding MS_RENAME = KeyBindingHelper.registerKeyBinding(new KeyBinding("Rename Server", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F2, "Not Enough Servers"));

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
                builder.suggest(server.name);
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
