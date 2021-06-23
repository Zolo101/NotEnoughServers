package dev.zelo.java.notenoughservers.mixins;

import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ServerList.class)
public interface ServerListAccessor {
    @Accessor
    List<ServerInfo> getServers();
}
