/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.render;

import net.minecraftforge.fluids.FluidAttributes;

public enum FluidDirection {
	UP(false),
	DOWN(false),
	RIGHT(false),
	LEFT(false),
	UP_GAS(true),
	DOWN_GAS(true),
	RIGHT_GAS(true),
	LEFT_GAS(true);

	private final boolean reversedGases;

	FluidDirection(boolean reversedGases) {
		this.reversedGases = reversedGases;
	}

	private FluidDirection resolve() {
		switch (this) {
			case UP:
			case DOWN:
			case RIGHT:
			case LEFT:
				return this;
			case UP_GAS:
				return UP;
			case DOWN_GAS:
				return DOWN;
			case RIGHT_GAS:
				return RIGHT;
			case LEFT_GAS:
				return LEFT;
			default:
				throw new IllegalStateException("Invalid Direction");
		}
	}

	private FluidDirection reverse() {
		switch (this) {
			case UP:
			case UP_GAS:
				return DOWN;
			case DOWN:
			case DOWN_GAS:
				return UP;
			case RIGHT:
			case RIGHT_GAS:
				return LEFT;
			case LEFT:
			case LEFT_GAS:
				return RIGHT;
			default:
				return null;
		}
	}

	public FluidDirection reverseIfGas(FluidAttributes attributes) {
		if (!reversedGases) {
			return this;
		}

		return attributes.isGaseous() ? this.reverse() : this.resolve();
	}
}
