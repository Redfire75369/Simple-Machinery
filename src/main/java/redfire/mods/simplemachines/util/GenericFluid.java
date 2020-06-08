package redfire.mods.simplemachines.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import redfire.mods.simplemachines.SimpleMachinery;

public class GenericFluid extends Fluid {
	public GenericFluid(String registryName) {
		super(registryName, new ResourceLocation(SimpleMachinery.modid, "fluids/" + registryName + "_still"), new ResourceLocation(SimpleMachinery.modid, "fluids/" + registryName + "_flow"));
	}
}
