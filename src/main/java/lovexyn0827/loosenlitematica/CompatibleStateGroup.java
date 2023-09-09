package lovexyn0827.loosenlitematica;

import java.util.Set;

import net.minecraft.block.BlockState;

public class CompatibleStateGroup {
	final Set<BlockState> states;

	public CompatibleStateGroup(Set<BlockState> states) {
		super();
		this.states = states;
	}
	
	public boolean isStateCompatible(BlockState s1, BlockState s2) {
		return this.states.contains(s1) && this.states.contains(s2);
	}
}
