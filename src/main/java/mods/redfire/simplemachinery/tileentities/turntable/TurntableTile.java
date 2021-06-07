package mods.redfire.simplemachinery.tileentities.turntable;

import mods.redfire.simplemachinery.registry.TileEntities;
import mods.redfire.simplemachinery.tileentities.machine.MachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TurntableTile extends MachineTile<TurntableRecipe> implements INamedContainerProvider {
	public static final int ITEM_INPUTS = 1;
	public static final int ITEM_OUTPUTS = 1;

	public TurntableTile() {
		super(TileEntities.TURNTABLE_TILE.get(), ITEM_INPUTS, ITEM_OUTPUTS, 0, 0, 0, 0, new EnergyCoil(10000, 100));
	}

	@Override
	protected Optional<TurntableRecipe> getRecipe() {
		return TurntableRecipe.getRecipe(level, inventory);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("screen.simplemachinery.turntable");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, @Nonnull PlayerInventory playerInv, @Nonnull PlayerEntity player) {
		return new TurntableContainer(windowId, level, worldPosition, playerInv, this);
	}
}
