package lovexyn0827.loosenlitematica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import fi.dy.masa.litematica.util.OverlayType;
import lovexyn0827.loosenlitematica.StateChecker;
import net.minecraft.block.BlockState;

@Mixin(ChunkRendererSchematicVbo.class)
public class ChunkRendererSchematicVboMixin {
	@Inject(method = "getOverlayType", at = @At("HEAD"), cancellable = true)
	private void checkState(BlockState stateSchematic, BlockState stateClient, CallbackInfoReturnable<OverlayType> cir) {
		if(StateChecker.check(stateSchematic, stateClient)) {
			cir.setReturnValue(OverlayType.NONE);
			cir.cancel();
		}
	}
}
