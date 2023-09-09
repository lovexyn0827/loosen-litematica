package lovexyn0827.loosenlitematica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import lovexyn0827.loosenlitematica.LLMod;
import net.minecraft.client.Keyboard;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
	@Inject(method = "processF3", at = @At(value = "HEAD"), cancellable = true)
	private void onF3Pressed(int key, CallbackInfoReturnable<Boolean> cir) {
		if(key == 'O') {
			LLMod.enabled = !LLMod.enabled;
			SchematicWorldRefresher.INSTANCE.updateAll();
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
