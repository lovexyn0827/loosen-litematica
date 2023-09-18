package lovexyn0827.loosenlitematica;

import net.minecraft.block.BlockState;

public interface ComparsionRule {
	boolean isCompatible(BlockState s1, BlockState s2);
	boolean validate();
	String getName();
}
