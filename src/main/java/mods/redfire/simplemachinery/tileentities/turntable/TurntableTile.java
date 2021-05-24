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

public class TurntableTile extends MachineTile<TurntableRecipe> implements INamedContainerProvider {
    public TurntableTile() {
        super(TileEntities.TILE_TURNTABLE.get(), 1, 1, new EnergyCoil(10000));
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
