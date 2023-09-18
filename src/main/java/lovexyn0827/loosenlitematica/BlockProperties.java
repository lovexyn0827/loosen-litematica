package lovexyn0827.loosenlitematica;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import lovexyn0827.loosenlitematica.mixin.FireBlockAccessor;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class BlockProperties {
	private static final Set<Class<?>> NO_SHAPE_CHECK_CLASS = ImmutableSet.of(
			PistonExtensionBlock.class, 
			ShulkerBoxBlock.class);
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
	
	private BlockProperties(BlockState state) {
		this.blockClass = state.getBlock().getClass();
		this.slipperiness = state.getBlock().getSlipperiness();
		this.velocityMultiplier = state.getBlock().getVelocityMultiplier();
		this.jumpVelocityMultiplier = state.getBlock().getJumpVelocityMultiplier();
		this.dynamicBounds = state.getBlock().hasDynamicBounds();
		this.resistance = state.getBlock().getBlastResistance();
		this.burnable = ((FireBlockAccessor) Blocks.FIRE).isBurnable(state);
		this.luminance = state.getLuminance();
		this.soild = Util.make(() -> {
			try {
				return state.isSolidBlock(null, BlockPos.ORIGIN);
			} catch (Exception e) {
				// XXX
				return false;
			}
		});
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
		return Objects.hash(blockClass, burnable, dynamicBounds, jumpVelocityMultiplier, luminance, soild,
				pistonBehavior, resistance, slipperiness, velocityMultiplier, fluid);
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
		return Objects.equals(blockClass, other.blockClass) && burnable == other.burnable
				&& dynamicBounds == other.dynamicBounds
				&& Float.floatToIntBits(jumpVelocityMultiplier) == Float.floatToIntBits(other.jumpVelocityMultiplier)
				&& luminance == other.luminance 
				&& soild == other.soild 
				&& opaque == other.opaque
				&& pistonBehavior == other.pistonBehavior
				&& Float.floatToIntBits(resistance) == Float.floatToIntBits(other.resistance)
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
					|| isShapePairTheSame(self.getCollisionShape(null, BlockPos.ORIGIN), 
							other.getCollisionShape(null, BlockPos.ORIGIN));
		} catch (NullPointerException e) {
			return false;
		}
	}

	private static boolean isShapePairTheSame(VoxelShape s0, VoxelShape s1) {
		return !VoxelShapes.matchesAnywhere(s0, s1, BooleanBiFunction.NOT_SAME);
	}
}
