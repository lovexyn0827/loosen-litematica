package lovexyn0827.loosenlitematica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
	@Invoker("isFlammable")
	boolean isBurnable(BlockState state);
}
