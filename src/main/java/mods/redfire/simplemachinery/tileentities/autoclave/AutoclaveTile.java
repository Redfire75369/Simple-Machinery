package mods.redfire.simplemachinery.tileentities.autoclave;

import mods.redfire.simplemachinery.registry.TileEntities;
import mods.redfire.simplemachinery.tileentities.machine.MachineTile;
import mods.redfire.simplemachinery.tileentities.machine.fluid.FluidMachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;

public class AutoclaveTile extends FluidMachineTile<AutoclaveRecipe> implements INamedContainerProvider {
	public static final int ITEM_INPUTS = 1;
	public static final int FLUID_INPUTS = 1;
	public static final int ITEM_OUTPUTS = 1;
	public static final int FLUID_FUELS = 1;

	public AutoclaveTile() {
		super(TileEntities.AUTOCLAVE_TILE.get(), Collections.singletonList(new MachineItemSlot()), Collections.singletonList(new MachineItemSlot()), Collections.singletonList(new MachineFluidTank(8000)), Collections.emptyList(), Collections.singletonList(new MachineFluidTank(8000, e -> e.getFluid().getRegistryName().getPath().equals("steam"))));
	}

	@Override
	protected Optional<AutoclaveRecipe> getRecipe() {
		return AutoclaveRecipe.getRecipe(level, getCombinedInputInv());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("screen.simplemachinery.autoclave");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, @Nonnull PlayerInventory playerInv, @Nonnull PlayerEntity player) {
		return new AutoclaveContainer(windowId, level, worldPosition, playerInv, this);
	}
}
