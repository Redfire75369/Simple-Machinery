/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util;

public class CoordinateChecker {
	public static boolean within(int coordinate, int minimum, int maximum) {
		return minimum < coordinate && coordinate < maximum;
	}

	public static boolean withinRectangle(int x, int y, int minX, int minY, int maxX, int maxY) {
		return within(x, minX, maxX) && within(y, minY, maxY);
	}
}
