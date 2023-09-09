package lovexyn0827.loosenlitematica;

import net.minecraft.block.BlockState;

public class StateChecker {
	public static boolean check(BlockState stateSchematic, BlockState stateClient) {
		if(LLMod.enabled && BulitinGroups.GROUPS_BY_STATE.containsKey(stateClient)) {
			return BulitinGroups.GROUPS_BY_STATE.get(stateClient).isStateCompatible(stateSchematic, stateClient);
		} else {
			return false;
		}
	}
}
