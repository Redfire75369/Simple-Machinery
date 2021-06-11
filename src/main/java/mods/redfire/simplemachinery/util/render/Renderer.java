/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.render;

import com.mojang.blaze3d.systems.RenderSystem;

public class Renderer {
	@SuppressWarnings("deprecation")
	public static void setGLColorFromInt(int color) {
		float alpha = ((color >> 24) & 0xff) / 255.0F;
		float red = ((color >> 16) & 0xff) / 255.0F;
		float green = ((color >> 8) & 0xff) / 255.0F;
		float blue = (color & 0xff) / 255.0F;

		RenderSystem.color4f(red, green, blue, alpha);
	}

	@SuppressWarnings("deprecation")
	public static void resetGLColor() {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
