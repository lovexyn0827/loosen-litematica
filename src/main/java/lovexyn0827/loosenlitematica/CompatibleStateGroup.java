package lovexyn0827.loosenlitematica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.block.BlockState;

public class CompatibleStateGroup {
	final Set<BlockState> states;

	public CompatibleStateGroup(Set<BlockState> states) {
		this.states = states;
	}
	
	public boolean isStateCompatible(BlockState s1, BlockState s2) {
		return this.states.contains(s1) && this.states.contains(s2);
	}
	
	public void writeJson(File f) {
		try(BufferedWriter w = new BufferedWriter(new FileWriter(f))){
			JsonWriter jw = new Gson().newJsonWriter(w);
			jw.beginArray();
			for(BlockState bs : this.states) {
				jw.value(bs.toString());
			}
			
			jw.endArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return {@code CompatibleStateGroup} represented by the JSON content in {@code f}, or {@code null};
	 */
	@Nullable
	public CompatibleStateGroup fromJson(File f) {
		try(BufferedReader r = new BufferedReader(new FileReader(f))) {
			JsonReader jr = new Gson().newJsonReader(r);
			jr.beginArray();
			Set<BlockState> states = new HashSet<>();
			while(jr.hasNext()) {
				states.add(toBlockState(jr.nextString()));
			}
			
			jr.endArray();
			return new CompatibleStateGroup(states);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static BlockState toBlockState(String nextString) {
		// TODO Auto-generated method stub
		return null;
	}
}
