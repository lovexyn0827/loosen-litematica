package lovexyn0827.loosenlitematica;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;

public class BulitinGroups {
	public static final Collection<CompatibleStateGroup> GROUPS;
	public static final Map<BlockState, CompatibleStateGroup> GROUPS_BY_STATE;
	
	static {
		Map<BlockProperties, Set<BlockState>> temp = new HashMap<>();
		long start = Util.getMeasuringTimeNano();
		Registries.BLOCK.forEach((b) -> {
			b.getStateManager().getStates().forEach((bs) -> {
				temp.computeIfAbsent(BlockProperties.of(bs), (bp) -> Sets.newHashSet()).add(bs);
			});
		});
		GROUPS = temp.values().stream()
				.sorted((s1, s2) -> s2.size() - s1.size())
				.filter((s) -> s.size() > 1)
				.map(CompatibleStateGroup::new)
				.toList();
		ImmutableMap.Builder<BlockState, CompatibleStateGroup> b = ImmutableMap.builder();
		temp.values().stream()
				.sorted((e1, e2) -> e2.size() - e1.size())
				.filter((e) -> e.size() > 1)
				.forEach((set) -> set.forEach((state) -> b.put(state, new CompatibleStateGroup(set))));
		GROUPS_BY_STATE = b.build();
		System.out.println((Util.getMeasuringTimeNano() - start) / 10E8D);
		System.out.println(GROUPS.size());
		GROUPS.getClass();
	}
}
