package dev.zelo.java.notenoughservers;

import com.mojang.brigadier.arguments.StringArgumentType;
//import dev.zelo.java.notenoughservers.commands.JoinserverSuggestor;
import dev.zelo.java.notenoughservers.mixins.ServerListAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;

public class NotEnoughServers implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-servers";

    @Override
    public void onInitializeClient() {
        ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("addserver")
                    .then(ClientCommandManager.argument("ip", StringArgumentType.string())
                            .then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
                                    .executes(context -> {

                                MinecraftClient client = MinecraftClient.getInstance();
                                String serverIP = StringArgumentType.getString(context, "ip");
                                String serverName = StringArgumentType.getString(context, "name");
                                String addressRaw = client.getNetworkHandler().getConnection().getAddress().toString();
                                String[] addresses = addressRaw.toLowerCase().split("/");
                                ServerList serverList = new ServerList(client);
                                boolean serverIPThis = serverIP.equalsIgnoreCase("this");
                                serverList.loadFile();
                                if (serverIPThis) {
                                    if (!client.isInSingleplayer()) {
                                        serverList.add(new ServerInfo(serverName, addresses[0], false));
                                    } else {
                                        Text failText = new LiteralText("NES: 'This' only works in multiplayer!")
                                                .styled((style -> style.withColor(Formatting.RED)));
                                        context.getSource().sendFeedback(failText);
                                        return 1;
                                    }
                                } else {
                                    serverList.add(new ServerInfo(serverName, serverIP, false));
                                }
                                serverList.saveFile();

                                String addressToShow = serverIPThis ? addresses[0] : serverIP;
                                Text successText = new LiteralText("NES: Successfully added '" + addressToShow + "'.")
                                        .styled((style -> style.withColor(Formatting.GREEN)));
                                context.getSource().sendFeedback(successText);
                                return 1;
                            }))));

//        ClientCommandManager.DISPATCHER.register(
//            ClientCommandManager.literal("joinserver")
//                    .then(ClientCommandManager.argument("name", StringArgumentType.string())
////                            .suggests(JoinserverSuggestor)
//                            .executes(context -> {
//
//                Text todoText = new LiteralText("Todo!")
//                        .styled((style -> style.withColor(Formatting.RED)));
//                context.getSource().sendFeedback(todoText);
//
//                MinecraftClient client = MinecraftClient.getInstance();
//                String serverName = StringArgumentType.getString(context, "name");
//                ServerList serverList = new ServerList(client);
//                serverList.loadFile();
//                List<ServerInfo> servers = ((ServerListAccessor) serverList).getServers();
//                Optional<ServerInfo> serverToJoin = servers.stream()
//                        .filter(serverInfo -> serverInfo.name.equals(serverName))
//                        .findFirst();
//
//                if (serverToJoin.isPresent()) {
//                    Text errorText = new LiteralText("Unknown server name!")
//                            .styled((style -> style.withColor(Formatting.RED)));
//
//                    client.disconnect();
//                    ConnectScreen.connect(client.currentScreen, client, ServerAddress.parse(serverToJoin.get().address), serverToJoin.get());
//                } else {
//                    Text errorText = new LiteralText("Unknown server name!")
//                            .styled((style -> style.withColor(Formatting.RED)));
//
////                    context.getSource().sendFeedback(errorText, false);
//                }
//                return 1;
//            })));
//        ClientCommandManager.DISPATCHER.register(
//            ClientCommandManager.literal("relog")
//                    .executes(context -> {
//
//                MinecraftClient client = MinecraftClient.getInstance();
//                ServerInfo server = client.getCurrentServerEntry();
//
//                client.disconnect();
//                ConnectScreen.connect(client.currentScreen, client, ServerAddress.parse(server.address), server);
//                return 1;
//            }));
    }
}
