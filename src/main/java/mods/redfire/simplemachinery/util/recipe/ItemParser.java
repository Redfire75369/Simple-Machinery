/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemParser {
	public static Ingredient parseIngredient(JsonObject json, String key) {
		if (JSONUtils.isStringValue(json, key)) {
			return Ingredient.of(getItemStack(JSONUtils.getAsString(json, key)));
		}

		JsonElement element = JSONUtils.isArrayNode(json, key)
				? JSONUtils.getAsJsonArray(json, key)
				: JSONUtils.getAsJsonObject(json, key);
		return Ingredient.fromJson(element);
	}

	public static List<Ingredient> parseIngredients(JsonObject json, String key) {
		if (!JSONUtils.isValidNode(json, key)) {
			return Collections.emptyList();
		} else if (JSONUtils.isStringValue(json, key)) {
			return Collections.singletonList(Ingredient.of(getItemStack(JSONUtils.getAsString(json, key))));
		}

		List<Ingredient> ingredients = new ArrayList<>();
		JsonArray array = JSONUtils.getAsJsonArray(json, key);
		for (JsonElement element : array) {
			if (element.isJsonObject()) {
				ingredients.add(Ingredient.fromJson(element));
			} else {
				ingredients.add(Ingredient.of(getItemStack(element.getAsString())));
			}
		}

		return ingredients;
	}

	public static ItemStack parseItem(JsonObject json, String key) {
		if (JSONUtils.isStringValue(json, key)) {
			return getItemStack(JSONUtils.getAsString(json, key));
		}

		return getItemStackFromJson(JSONUtils.getAsJsonObject(json, key));
	}

	public static List<ItemStack> parseItems(JsonObject json, String key) {
		if (!JSONUtils.isValidNode(json, key)) {
			return Collections.emptyList();
		} else if (JSONUtils.isStringValue(json, key)) {
			return Collections.singletonList(getItemStack(JSONUtils.getAsString(json, key)));
		}

		List<ItemStack> items = new ArrayList<>();
		JsonArray array = JSONUtils.getAsJsonArray(json, key);
		for (JsonElement element : array) {
			if (element.isJsonObject()) {
				items.add(getItemStackFromJson(element.getAsJsonObject()));
			} else {
				items.add(getItemStack(element.getAsString()));
			}
		}

		return items;
	}

	public static Tuple<ItemStack, Float> parseWeightedItem(JsonObject json, String key) {
		if (JSONUtils.isStringValue(json, key)) {
			return new Tuple<>(getItemStack(JSONUtils.getAsString(json, key)), 1.0F);
		}

		return getWeightedItemFromJson(JSONUtils.getAsJsonObject(json, key));
	}

	public static List<Tuple<ItemStack, Float>> parseWeightedItems(JsonObject json, String key) {
		if (!JSONUtils.isValidNode(json, key)) {
			return Collections.emptyList();
		} else if (JSONUtils.isStringValue(json, key)) {
			return Collections.singletonList(new Tuple<>(getItemStack(JSONUtils.getAsString(json, key)), 1.0F));
		}

		List<Tuple<ItemStack, Float>> weightedItems = new ArrayList<>();
		JsonArray array = JSONUtils.getAsJsonArray(json, key);
		for (JsonElement element : array) {
			if (element.isJsonObject()) {
				weightedItems.add(getWeightedItemFromJson(element.getAsJsonObject()));
			} else {
				weightedItems.add(new Tuple<>(getItemStack(element.getAsString()), 1.0F));
			}
		}

		return weightedItems;
	}

	public static Tuple<List<ItemStack>, List<Float>> mapWeightedItems(List<Tuple<ItemStack, Float>> weightedItems) {
		return new Tuple<>(weightedItems.stream().map(Tuple::getA).collect(Collectors.toList()),
				weightedItems.stream().map(Tuple::getB).collect(Collectors.toList()));
	}

	private static ItemStack getItemStack(String string) {
		return new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string)))
				.orElseThrow(() -> new IllegalStateException("Item: " + string + " does not exist.")));
	}

	private static ItemStack getItemStackFromJson(JsonObject json) {
		return new ItemStack(getItemStack(JSONUtils.getAsString(json, "name")).getItem(), JSONUtils.getAsInt(json, "count", 1));
	}

	private static Tuple<ItemStack, Float> getWeightedItemFromJson(JsonObject json) {
		return new Tuple<>(new ItemStack(getItemStack(JSONUtils.getAsString(json, "name")).getItem(), JSONUtils.getAsInt(json, "count", 1)), JSONUtils.getAsFloat(json, "chance", 1.0F));
	}
}
