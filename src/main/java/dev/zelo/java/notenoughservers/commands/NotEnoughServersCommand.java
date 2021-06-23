package dev.zelo.java.notenoughservers.commands;
/*
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class NotEnoughServersCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode node = registerMain(dispatcher);
        dispatcher.register(literal("addserver")
            .redirect(node));
    }

    public static LiteralCommandNode registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(literal("addserver")
            .then(CommandManager.argument("ip", StringArgumentType.string())
                .then(CommandManager.argument("name", StringArgumentType.string()))
                    .executes(context -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        String serverIP = StringArgumentType.getString(context, "ip");
                        String serverName = StringArgumentType.getString(context, "name");
                        ServerList serverList = new ServerList(client);
                        serverList.loadFile();
                        if (serverIP.equals("this") && !client.isInSingleplayer()) {
                            serverList.add(new ServerInfo(serverName, client.getNetworkHandler().getConnection().getAddress().toString(), false));
                        } else {
                            serverList.add(new ServerInfo(serverName, serverIP, false));
                        }
                        serverList.saveFile();

                        Text successText = new LiteralText("NES: Successfully added " + serverIP)
                                .styled((style -> style.withColor(Formatting.GREEN).withBold(true)));
                        context.getSource().sendFeedback(successText, false);
                        return 1;
                    })));
    }
}
*/