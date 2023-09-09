package lovexyn0827.loosenlitematica.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lovexyn0827.loosenlitematica.BulitinGroups;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Inject(method = "tick", at = @At(value = "RETURN"))
	public void onTicked(BooleanSupplier bs, CallbackInfo ci) {
		BulitinGroups.GROUPS.getClass();
	}
}
