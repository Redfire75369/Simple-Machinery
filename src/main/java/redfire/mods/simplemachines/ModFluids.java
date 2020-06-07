package redfire.mods.simplemachines;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import redfire.mods.simplemachines.util.GenericFluid;

public class ModFluids {
	public static final GenericFluid steam = new GenericFluid("steam");

	public static void init() {
		FluidRegistry.registerFluid(steam);
		FluidRegistry.addBucketForFluid(steam);
	}

	public static boolean isValidSteamStack(FluidStack stack) {
		return getFluidFromStack(stack) == steam;
	}

	public static GenericFluid getFluidFromStack(FluidStack stack) {
		return stack == null ? null : (GenericFluid) stack.getFluid();
	}

	public static String getFluidName(FluidStack stack) {
		return getFluidName(getFluidFromStack(stack));
	}
	public static String getFluidName(GenericFluid fluid) {
		return fluid == null ? null : fluid.getName();
	}

	public static int getFluidAmount(FluidStack stack) {
		return stack == null ? 0 : stack.amount;
	}
}
