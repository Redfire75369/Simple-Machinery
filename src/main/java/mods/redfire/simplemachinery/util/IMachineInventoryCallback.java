/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util;

public interface IMachineInventoryCallback {
	default void onInventoryChange(int slot) {}

	default void onTankChange(int tank) {}

	default boolean clearSlot(int slot) {
		return false;
	}

	default boolean clearTank(int tank) {
		return false;
	}

	default boolean clearEnergy(int coil) {
		return false;
	}
}
