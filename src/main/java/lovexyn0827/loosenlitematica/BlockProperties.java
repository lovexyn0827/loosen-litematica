package lovexyn0827.loosenlitematica;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import lovexyn0827.loosenlitematica.mixin.VoxelShapeAccessor;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class BlockProperties {
	private static final Set<Class<?>> NO_SHAPE_CHECK_CLASS = ImmutableSet.of(
			PistonExtensionBlock.class, 
			ShulkerBoxBlock.class);
	private static final ImmutableMap<BlockState, Set<BlockState>> MANDATED_EQVIVALENT_CLASSES = Util.make(() -> {
		Set<ImmutableSet<BlockState>> unindexed = new HashSet<>();
		unindexed.add(ImmutableSet.of(Blocks.GLASS.getDefaultState(), 
				Blocks.BLACK_STAINED_GLASS.getDefaultState(), 
				Blocks.BLUE_STAINED_GLASS.getDefaultState(), 
				Blocks.BROWN_STAINED_GLASS.getDefaultState(), 
				Blocks.CYAN_STAINED_GLASS.getDefaultState(), 
				Blocks.GRAY_STAINED_GLASS.getDefaultState(), 
				Blocks.GREEN_STAINED_GLASS.getDefaultState(), 
				Blocks.LIGHT_BLUE_STAINED_GLASS.getDefaultState(), 
				Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), 
				Blocks.LIME_STAINED_GLASS.getDefaultState(), 
				Blocks.MAGENTA_STAINED_GLASS.getDefaultState(), 
				Blocks.ORANGE_STAINED_GLASS.getDefaultState(), 
				Blocks.PINK_STAINED_GLASS.getDefaultState(), 
				Blocks.PURPLE_STAINED_GLASS.getDefaultState(), 
				Blocks.RED_STAINED_GLASS.getDefaultState(), 
				Blocks.WHITE_STAINED_GLASS.getDefaultState(), 
				Blocks.YELLOW_STAINED_GLASS.getDefaultState()));
		unindexed.add(ImmutableSet.of(Blocks.GLASS_PANE.getDefaultState(), 
				Blocks.BLACK_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.BLUE_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.CYAN_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.GRAY_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.GREEN_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.LIME_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.MAGENTA_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.ORANGE_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.PINK_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.PURPLE_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.RED_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.WHITE_STAINED_GLASS_PANE.getDefaultState(), 
				Blocks.YELLOW_STAINED_GLASS_PANE.getDefaultState()));
		ImmutableMap.Builder<BlockState, Set<BlockState>> out = ImmutableMap.builder();
		for (Set<BlockState> set : unindexed) {
			for (BlockState state : set) {
				out.put(state, set);
			}
		}
		
		return out.build();
	});
	private static final Predicate<BlockState> FORCE_STATE_AND_OWNER_PRED = (s) -> {
		return s.emitsRedstonePower()
				|| s.contains(Properties.POWERED) 
						&& !(s.isIn(BlockTags.TRAPDOORS) 
								|| s.isIn(BlockTags.DOORS) 
								|| s.isOf(Blocks.NOTE_BLOCK)
								|| s.isIn(BlockTags.FENCE_GATES))
				|| s.contains(Properties.PISTON_TYPE)
				|| s.contains(Properties.POWER)
				|| s.getBlock() instanceof PistonBlock
				|| s.getBlock() instanceof AbstractRailBlock;
	};
	private static final Predicate<BlockState> FORCE_STATE_PRED = (s) -> {
		return false;
	};
	private static final Set<Property<?>> PROPERTY_BLACKLIST = ImmutableSet.of();
	private static final Set<Property<?>> PROPERTY_WHITELIST = ImmutableSet.of();
//			ImmutableSet.of(
//					PistonBlock.class, 
//					PressurePlateBlock.class, 
//					AbstractRailBlock.class, 
//					);
	public final Class<?> blockClass;
	public final float slipperiness;
	public final float velocityMultiplier;
	public final float jumpVelocityMultiplier;
	public final boolean dynamicBounds;
	public final float resistance;
	public final boolean burnable;
	public final int luminance;
	public final boolean soild;
	public final boolean opaque;
	public final PistonBehavior pistonBehavior;
	public final BlockState state;
	public final FluidState fluid;
	
	@SuppressWarnings("deprecation")
	private BlockProperties(BlockState state) {
		this.blockClass = state.getBlock().getClass();
		this.slipperiness = state.getBlock().getSlipperiness();
		this.velocityMultiplier = state.getBlock().getVelocityMultiplier();
		this.jumpVelocityMultiplier = state.getBlock().getJumpVelocityMultiplier();
		this.dynamicBounds = state.getBlock().hasDynamicBounds();
		this.resistance = state.getBlock().getBlastResistance();
		this.burnable = state.isBurnable();
		this.luminance = state.getLuminance();
		this.soild = state.isSolid();
		this.opaque = state.isOpaque();
		this.pistonBehavior = state.getPistonBehavior();
		this.state = state;
		this.fluid = state.getFluidState();
	}
	
	public static BlockProperties of(BlockState bs) {
		return new BlockProperties(bs);
	}

	@Override
	public int hashCode() {
		if (MANDATED_EQVIVALENT_CLASSES.containsKey(this.state)) {
			return MANDATED_EQVIVALENT_CLASSES.get(this.state).hashCode();
		} else {
			return Objects.hash(this.blockClass);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		BlockProperties other = (BlockProperties) obj;
		if (MANDATED_EQVIVALENT_CLASSES.containsKey(this.state) 
				&& MANDATED_EQVIVALENT_CLASSES.get(this.state).contains(other.state)) {
			return true;
		}
		
		return Objects.equals(blockClass, other.blockClass) 
				&& burnable == other.burnable
				&& dynamicBounds == other.dynamicBounds
				&& Float.floatToIntBits(jumpVelocityMultiplier) == Float.floatToIntBits(other.jumpVelocityMultiplier)
				&& luminance == other.luminance 
				&& soild == other.soild 
				&& opaque == other.opaque
				&& pistonBehavior == other.pistonBehavior
				&& !((resistance >= 9.0F || resistance < 0) ^ (other.resistance >= 9.0F || other.resistance < 0))
				&& Float.floatToIntBits(slipperiness) == Float.floatToIntBits(other.slipperiness)
				&& Float.floatToIntBits(velocityMultiplier) == Float.floatToIntBits(other.velocityMultiplier)
				&& shapesMatch(this.state, other.state)
				&& fluid.isOf(other.fluid.getFluid())
				&& fluid.getLevel() == other.fluid.getLevel()
				&& checkStates(state, other.state);
	}

	private static boolean checkStates(BlockState s0, BlockState s1) {
		if(checkWhiteListedProperties(s0, s1)) {
			if(FORCE_STATE_PRED.test(s0) || FORCE_STATE_PRED.test(s1)) {
				return allPropertiesMatches(s0, s1);
			} else if(FORCE_STATE_AND_OWNER_PRED.test(s0) || FORCE_STATE_AND_OWNER_PRED.test(s1)) {
				return s0 == s1;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	private static boolean checkWhiteListedProperties(BlockState s0, BlockState s1) {
		return PROPERTY_WHITELIST.stream().allMatch((p) -> {
			return !s0.contains(p) || !s1.contains(p) || s0.get(p).equals(s1.get(p));
		});
	}

	private static boolean allPropertiesMatches(BlockState s0, BlockState s1) {
		return s0.getProperties().equals(s1.getProperties()) && s0.getProperties().stream().allMatch((p) -> {
			return PROPERTY_BLACKLIST.contains(p) || s0.get(p).equals(s1.get(p));
		});
	}

	private static boolean shapesMatch(BlockState self, BlockState other) {
		try {
			return NO_SHAPE_CHECK_CLASS.contains(self.getBlock().getClass()) 
					|| NO_SHAPE_CHECK_CLASS.contains(other.getBlock().getClass())
					|| isShapePairTheSame(self.getOutlineShape(null, BlockPos.ORIGIN), 
							other.getOutlineShape(null, BlockPos.ORIGIN));
		} catch (NullPointerException e) {
			return false;
		}
	}

	private static boolean isShapePairTheSame(VoxelShape s0, VoxelShape s1) {
		try {
			VoxelSet v0 = ((VoxelShapeAccessor) s0).getVoxels$0827();
			VoxelSet v1 = ((VoxelShapeAccessor) s1).getVoxels$0827();
			if(v0.getClass() != v1.getClass() || s0.getClass() != s1.getClass()) {
				return false;
			}
			
			if(!s0.getBoundingBox().equals(s1.getBoundingBox())) {
				return false;
			}
			
			if(!s0.getBoundingBoxes().equals(s1.getBoundingBoxes())) {
				return false;
			}
			
			if(!((VoxelShapeAccessor) s0).getPointPositions$0827(Direction.Axis.X).equals(((VoxelShapeAccessor) s1).getPointPositions$0827(Direction.Axis.X))
					|| !((VoxelShapeAccessor) s0).getPointPositions$0827(Direction.Axis.Y).equals(((VoxelShapeAccessor) s1).getPointPositions$0827(Direction.Axis.Y))
					|| !((VoxelShapeAccessor) s0).getPointPositions$0827(Direction.Axis.Z).equals(((VoxelShapeAccessor) s1).getPointPositions$0827(Direction.Axis.Z))) {
				return false;
			}
			
			return !VoxelShapes.matchesAnywhere(s0, s1, BooleanBiFunction.NOT_SAME);
		} catch (Exception e) {
			return false;
		}
	}
}
