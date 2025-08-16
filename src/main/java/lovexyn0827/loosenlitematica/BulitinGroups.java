package lovexyn0827.loosenlitematica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Util;

public class BulitinGroups {
	public static final File CACHED_GROUPS;
	public static final Map<BlockState, CompatibleStateGroup> GROUPS_BY_STATE;
	
	static {
		CACHED_GROUPS = new File("builtin_state_groups_" + SharedConstants.getGameVersion().name() + ".json");
		Collection<CompatibleStateGroup> groups;
		long start = Util.getMeasuringTimeNano();
		try(BufferedReader r = new BufferedReader(new FileReader(CACHED_GROUPS))) {
			RegistryWrapper<Block> reg = Registries.BLOCK;
			groups = new HashSet<>();
			JsonReader jr = new Gson().newJsonReader(r);
			jr.beginArray();
			while (jr.hasNext()) {
				groups.add(CompatibleStateGroup.fromJson(reg, jr));
			}
			
			jr.endArray();
		} catch (Throwable e) {
			e.printStackTrace();
			Map<BlockProperties, Set<BlockState>> temp = new HashMap<>();
			Registries.BLOCK.forEach((b) -> {
				b.getStateManager().getStates().forEach((bs) -> {
					temp.computeIfAbsent(BlockProperties.of(bs), (bp) -> Sets.newHashSet()).add(bs);
				});
			});

			groups = temp.values().stream()
					.filter((s) -> s.size() > 1)
					.map(CompatibleStateGroup::new)
					.toList();
			try(BufferedWriter w = new BufferedWriter(new FileWriter(CACHED_GROUPS))){
				JsonWriter jw = new Gson().newJsonWriter(w);
				jw.beginArray();
				for (CompatibleStateGroup g : groups) {
					g.writeJson(jw);
				}
				
				jw.endArray();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
		ImmutableMap.Builder<BlockState, CompatibleStateGroup> b = ImmutableMap.builder();
		groups.stream()
				.sorted((e1, e2) -> e2.size() - e1.size())
				.forEach((set) -> set.forEach((state) -> b.put(state, set)));
		GROUPS_BY_STATE = b.build();
		System.out.println((Util.getMeasuringTimeNano() - start) / 10E9D);
		System.out.println(groups.size());
	}
}
