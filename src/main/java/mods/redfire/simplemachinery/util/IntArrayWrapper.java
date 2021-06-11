/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.IIntArray;

public class IntArrayWrapper implements IIntArray {
	private final IntList list;

	public IntArrayWrapper(int capacity) {
		list = new IntArrayList(capacity);
		for (int i = 0; i < capacity; i++) {
			list.add(0);
		}
	}

	@Override
	public int get(int index) {
		if (index >= 0 && index < getCount()) {
			if (index % 2 == 0) {
				return getInternal(index / 2) & 0xffff;
			} else {
				return (getInternal((index - 1) / 2) >> 16) & 0xffff;
			}
		} else {
			return 0;
		}
	}

	@Override
	public void set(int index, int value) {
		if (index >= 0 && index < getCount()) {
			if (index % 2 == 0) {
				setInternal(index / 2, (get(index + 1) << 16) | (value & 0xffff));
			} else {
				setInternal((index - 1) / 2, (value << 16) | (get(index - 1) & 0xffff));
			}
		}
	}

	@Override
	public int getCount() {
		return list.size() * 2;
	}

	public int getInternal(int index) {
		return list.getInt(index);
	}

	public void setInternal(int index, int value) {
		list.set(index, value);
	}
}
