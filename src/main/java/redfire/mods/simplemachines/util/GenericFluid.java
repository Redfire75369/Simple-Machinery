package redfire.mods.simplemachines.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import redfire.mods.simplemachines.SimpleMachines;

public class GenericFluid extends Fluid {
	public GenericFluid(String registryName) {
		super(registryName, new ResourceLocation(SimpleMachines.modid, "fluids/" + registryName + "_still"), new ResourceLocation(SimpleMachines.modid, "fluids/" + registryName + "_flow"));
	}
}
