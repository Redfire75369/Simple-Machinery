/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.util.Corner;
import mods.redfire.simplemachinery.util.fluid.MachineFluidInventory;
import mods.redfire.simplemachinery.util.render.FluidDirection;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import static mods.redfire.simplemachinery.SimpleMachinery.MODID;
import static mods.redfire.simplemachinery.util.render.Renderer.resetGLColor;
import static mods.redfire.simplemachinery.util.render.Renderer.setGLColorFromInt;

public abstract class MachineScreen<T extends MachineContainer<?>> extends ContainerScreen<T> {
	protected final ResourceLocation gui;

	protected final int xSize;
	protected final int ySize;

	protected MachineScreen(int xSize, int ySize, String guiLocation, T container, PlayerInventory inv, ITextComponent name) {
		super(container, inv, name);

		gui = new ResourceLocation(MODID, guiLocation);
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {}

	protected void drawTexturedRectangle(@Nonnull MatrixStack matrixStack, TextureAtlasSprite sprite, int x, int y, int width, int height, Corner corner) {
		switch (corner) {
			case TopLeft:
				blit(matrixStack, x, y, 100, width, height, sprite);
				break;
			case BottomLeft:
				blit(matrixStack, x, y + 16 - height, 100, width, height, sprite);
				break;
		}
	}

	protected void drawGui(@Nonnull MatrixStack matrixStack) {
		minecraft.getTextureManager().bind(gui);
		resetGLColor();
		this.blit(matrixStack, getGuiLeft(), getGuiTop(), 0, 0, xSize, ySize);
	}

	protected void drawProgress(@Nonnull MatrixStack matrixStack, int xPos, int yPos, int width, int height) {
		minecraft.getTextureManager().bind(gui);
		resetGLColor();
		blit(matrixStack, getGuiLeft() + xPos, getGuiTop() + yPos, xSize + 2, 1, getProgressScaled(width), height);
	}

	protected void drawTank(@Nonnull MatrixStack matrixStack, int xPos, int yPos, int width, int height, int tankIndex, @Nonnull FluidDirection direction) {
		FluidStack fluid = menu.tile.getTank(tankIndex).getFluidStack();
		FluidAttributes attributes = fluid.getFluid().getAttributes();

		if (fluid.isEmpty() || fluid.getFluid() == null) {
			return;
		}

		direction = direction.reverseIfGas(attributes);
		int dimension = direction == FluidDirection.UP || direction == FluidDirection.DOWN ? height : width;
		int scaledDimension = fluid.getAmount() > 0 ? Math.min(Math.max(1, getTankScaled(tankIndex, dimension)), dimension) : 0;

		minecraft.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
		TextureAtlasSprite sprite = minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(attributes.getStillTexture());

		setGLColorFromInt(attributes.getColor());
		drawTankInternal(matrixStack, sprite, getGuiLeft() + xPos, getGuiTop() + yPos, width, height, scaledDimension, direction);
	}

	protected void drawTankInternal(@Nonnull MatrixStack matrixStack, @Nonnull TextureAtlasSprite sprite, int xPos, int yPos, int containerWidth, int containerHeight, int dimension, @Nonnull FluidDirection direction) {
		int yStart = yPos + containerHeight;

		int xTileCount;
		int yTileCount;

		switch (direction) {
			case UP:
			case DOWN:
			case UP_GAS:
			case DOWN_GAS:
				xTileCount = containerWidth / 16;
				yTileCount = dimension / 16;
				break;
			case RIGHT:
			case LEFT:
			case RIGHT_GAS:
			case LEFT_GAS:
				xTileCount = dimension / 16;
				yTileCount = containerHeight / 16;
				break;
			default:
				throw new IllegalStateException("Invalid Direction");
		}

		switch (direction) {
			case UP:
				for (int xTileIndex = 0; xTileIndex < xTileCount + 1; xTileIndex++) {
					for (int yTileIndex = 0; yTileIndex < yTileCount + 1; yTileIndex++) {
						int width = xTileIndex == xTileCount ? containerWidth % 16 : 16;
						int height = yTileIndex == yTileCount ? dimension % 16 : 16;

						int x = xPos + xTileIndex * 16;
						int y = yStart - (yTileIndex + 1) * 16;

						if (width > 0 && height > 0) {
							drawTexturedRectangle(matrixStack, sprite, x, y, width, height, Corner.BottomLeft);
						}
					}
				}
				break;
			case RIGHT:
				for (int xTileIndex = 0; xTileIndex < xTileCount + 1; xTileIndex++) {
					for (int yTileIndex = 0; yTileIndex < yTileCount + 1; yTileIndex++) {
						int width = xTileIndex == xTileCount ? dimension % 16 : 16;
						int height = yTileIndex == yTileCount ? containerHeight % 16 : 16;

						int x = xPos + xTileIndex * 16;
						int y = yStart - (yTileIndex + 1) * 16;

						if (width > 0 && height > 0) {
							drawTexturedRectangle(matrixStack, sprite, x, y, width, height, Corner.BottomLeft);
						}
					}
				}
				break;
			case DOWN:
				for (int xTileIndex = 0; xTileIndex < xTileCount + 1; xTileIndex++) {
					for (int yTileIndex = 0; yTileIndex < yTileCount + 1; yTileIndex++) {
						int width = xTileIndex == xTileCount ? containerWidth % 16 : 16;
						int height = yTileIndex == yTileCount ? dimension % 16 : 16;

						int x = xPos + xTileIndex * 16;
						int y = yPos + yTileIndex * 16;

						if (width > 0 && height > 0) {
							drawTexturedRectangle(matrixStack, sprite, x, y, width, height, Corner.TopLeft);
						}
					}
				}
				break;
		}
	}

	protected void drawEnergy(@Nonnull MatrixStack matrixStack, int xPos, int yPos, int width, int height) {
		minecraft.getTextureManager().bind(gui);
		resetGLColor();
		blit(matrixStack, getGuiLeft() + xPos, getGuiTop() + yPos, 1, ySize + 2, getEnergyScaled(width), height);
	}

	protected int getProgressScaled(int pixels) {
		int i = menu.tile.getProgress();
		int j = menu.tile.getTotalProgress();
		return j != 0 ? (j - i) * pixels / j : 0;
	}

	protected int getTankScaled(int tank, int pixels) {
		MachineFluidInventory tankInventory = menu.tile.getFluidInv();

		int i = tank >= 0 && tank < tankInventory.getTanks() ? tankInventory.get(tank).getAmount() : 0;
		int j = tank >= 0 && tank < tankInventory.getTanks() ? tankInventory.getTank(tank).getCapacity() : 0;
		return j != 0 ? i * pixels / j : 0;
	}

	protected int getEnergyScaled(int pixels) {
		int i = menu.tile.getCoil().getEnergyStored();
		return i * pixels / 10000;
	}
}
