package lovexyn0827.loosenlitematica;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.RegistryWrapper;

public class CompatibleStateGroup {
	final Set<BlockState> states;

	public CompatibleStateGroup(Set<BlockState> states) {
		this.states = states;
	}
	
	public boolean isStateCompatible(BlockState s1, BlockState s2) {
		return this.states.contains(s1) && this.states.contains(s2);
	}
	
	public void writeJson(JsonWriter jw) throws IOException {
		jw.beginArray();
		for(BlockState bs : this.states) {
			jw.value(bs.toString());
		}
		
		jw.endArray();
	}
	
	/**
	 * @return {@code CompatibleStateGroup} represented by the JSON content in {@code f}, or {@code null};
	 * @throws IOException 
	 */
	@Nullable
	public static CompatibleStateGroup fromJson(RegistryWrapper<Block> reg, JsonReader jr) throws IOException {
		jr.beginArray();
		Set<BlockState> states = new HashSet<>();
		while(jr.hasNext()) {
			BlockState state = toBlockState(reg, jr.nextString());
			if (state != null) {
				states.add(state);
			}
		}
		
		jr.endArray();
		return new CompatibleStateGroup(states);
	}

	private static BlockState toBlockState(RegistryWrapper<Block> reg, String in) {
		try {
			return BlockArgumentParser.block(reg, new StringReader(in), false).blockState();
		} catch (CommandSyntaxException e) {
			return null;
		}
	}

	public int size() {
		return this.states.size();
	}

	public void forEach(Consumer<BlockState> act) {
		this.states.forEach(act);
	}
}
