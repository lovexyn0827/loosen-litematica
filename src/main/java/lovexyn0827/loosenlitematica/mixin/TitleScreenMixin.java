package lovexyn0827.loosenlitematica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lovexyn0827.loosenlitematica.BulitinGroups;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
	@Inject(method = "<clinit>", at = @At(value = "RETURN"))
	private static void onClientInit(CallbackInfo ci) {
		new Thread(() -> BulitinGroups.GROUPS_BY_STATE.getClass()).start();	// Trigger group generation
	}
}
