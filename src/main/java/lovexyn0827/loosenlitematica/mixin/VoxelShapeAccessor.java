package lovexyn0827.loosenlitematica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;

@Mixin(VoxelShape.class)
public interface VoxelShapeAccessor {
	@Accessor("voxels")
	VoxelSet getVoxels$0827();
	
	@Invoker("getPointPositions")
	public DoubleList getPointPositions$0827(Direction.Axis axis);
}
