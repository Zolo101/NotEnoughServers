package dev.zelo.java.notenoughservers.mixins;
/*
import com.mojang.brigadier.CommandDispatcher;
import dev.zelo.java.notenoughservers.commands.NotEnoughServersCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void CommandManager(CommandManager.RegistrationEnvironment environment, CallbackInfo ci) {
        NotEnoughServersCommand.register(this.dispatcher);
    }
}
*/
