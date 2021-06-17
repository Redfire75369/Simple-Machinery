/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.minecraftforge.fluids.FluidAttributes.BUCKET_VOLUME;

public class FluidParser {
	public static FluidStack parseFluid(JsonObject json, String key) {
		if (JSONUtils.isStringValue(json, key)) {
			return getFluidStack(JSONUtils.getAsString(json, key));
		}

		return getFluidStackFromJson(JSONUtils.getAsJsonObject(json, key));
	}

	public static List<FluidStack> parseFluids(JsonObject json, String key) {
		if (!JSONUtils.isValidNode(json, key)) {
			return Collections.emptyList();
		} else if (JSONUtils.isStringValue(json, key)) {
			return Collections.singletonList(getFluidStack(JSONUtils.getAsString(json, key)));
		}

		List<FluidStack> fluids = new ArrayList<>();
		JsonArray array = JSONUtils.getAsJsonArray(json, key);
		for (JsonElement element : array) {
			if (element.isJsonObject()) {
				fluids.add(getFluidStackFromJson(element.getAsJsonObject()));
			} else {
				fluids.add(getFluidStack(element.getAsString()));
			}
		}

		return fluids;
	}

	private static FluidStack getFluidStack(String string) {
		return new FluidStack(Optional.ofNullable(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(string)))
				.orElseThrow(() -> new IllegalStateException("Fluid: " + string + " does not exist.")), BUCKET_VOLUME);
	}

	private static FluidStack getFluidStackFromJson(JsonObject json) {
		return new FluidStack(getFluidStack(JSONUtils.getAsString(json, "name")).getFluid(), JSONUtils.getAsInt(json, "amount", 1000));
	}
}
