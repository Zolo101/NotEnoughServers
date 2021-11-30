//package dev.zelo.java.notenoughservers.commands;
//
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.suggestion.SuggestionProvider;
//import com.mojang.brigadier.suggestion.Suggestions;
//import com.mojang.brigadier.suggestion.SuggestionsBuilder;
//import dev.zelo.java.notenoughservers.mixins.ServerListAccessor;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.ServerInfo;
//import net.minecraft.client.option.ServerList;
//import net.minecraft.server.command.ServerCommandSource;
//import net.minecraft.util.Identifier;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//public class JoinserverSuggestor implements SuggestionProvider<ServerCommandSource> {
//    @Override
//    public <S> CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
//        Identifier nameTypeID = context.getArgument("name", Identifier.class);
//        MinecraftClient client = MinecraftClient.getInstance();
//        ServerList serverList = new ServerList(client);
//        serverList.loadFile();
//        List<ServerInfo> servers = ((ServerListAccessor) serverList).getServers();
//
//        for (ServerInfo server : servers) {
//            builder.suggest(server.name);
//        }
//
//        return builder.buildFuture();
//    }
//}
