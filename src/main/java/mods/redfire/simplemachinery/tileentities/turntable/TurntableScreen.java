package mods.redfire.simplemachinery.tileentities.turntable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.tileentities.machine.MachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class TurntableScreen extends MachineScreen<TurntableContainer> {
	private static final ResourceLocation GUI = new ResourceLocation(SimpleMachinery.MODID, "textures/screen/container/turntable.png");

	public TurntableScreen(TurntableContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, container, inv, name);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bind(GUI);

		int x = getGuiLeft();
		int y = getGuiTop();

		this.blit(matrixStack, x, y, 0, 0, xSize, ySize);

		this.blit(matrixStack, x + 76, y + 25, xSize + 1, 0, getTimeScaled(24), 18);
		this.blit(matrixStack, x + 24, y + 65, 1, ySize + 1, getEnergyStorageScaled(128), 10);
	}
}
