package lovexyn0827.loosenlitematica.mixin;

import java.util.HashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import fi.dy.masa.litematica.schematic.verifier.SchematicVerifier;
import lovexyn0827.loosenlitematica.StateChecker;
import net.minecraft.block.BlockState;

@Mixin(SchematicVerifier.class)
public class SchematicVerifierMixin {
	@Redirect(
			method = "checkBlockStates", 
			at = @At(
					value = "INVOKE", 
					target = "java/util/HashSet.contains(Ljava/lang/Object;)Z"
			)
	)
	private boolean checkBlockStates(HashSet<?> set, Object p, int x, int y, int z, 
			BlockState stateSchematic, BlockState stateClient) {
		return set.contains(p) || StateChecker.check(stateSchematic, stateClient);
	}
}
