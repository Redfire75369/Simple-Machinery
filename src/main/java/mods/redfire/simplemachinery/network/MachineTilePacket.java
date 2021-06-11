/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.network;

import mods.redfire.simplemachinery.tileentities.machine.MachineContainer;
import mods.redfire.simplemachinery.util.fluid.MachineFluidInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MachineTilePacket {
	public List<FluidStack> fluids = new ArrayList<>();
	public int energy;
	public int progress;
	public int totalProgress;
	protected BlockPos pos;

	public MachineTilePacket() {}

	public MachineTilePacket(BlockPos pos, MachineFluidInventory tankInventory, int energyStored, int progress, int totalProgress) {
		this.pos = pos;
		for (int i = 0; i < tankInventory.getTanks(); i++) {
			fluids.add(tankInventory.get(i));
		}

		energy = energyStored;
		this.progress = progress;
		this.totalProgress = totalProgress;
	}

	public static void encode(MachineTilePacket packet, PacketBuffer buffer) {
		buffer.writeBlockPos(packet.pos);
		buffer.writeInt(packet.fluids.size());
		for (FluidStack fluid : packet.fluids) {
			buffer.writeFluidStack(fluid);
		}

		buffer.writeInt(packet.energy);
		buffer.writeInt(packet.progress);
		buffer.writeInt(packet.totalProgress);
	}

	public static MachineTilePacket decode(PacketBuffer buffer) {
		MachineTilePacket packet = new MachineTilePacket();

		packet.pos = buffer.readBlockPos();
		int fluidTanks = buffer.readInt();
		for (int i = 0; i < fluidTanks; i++) {
			packet.fluids.add(buffer.readFluidStack());
		}

		packet.energy = buffer.readInt();
		packet.progress = buffer.readInt();
		packet.totalProgress = buffer.readInt();

		return packet;
	}

	public static void consume(MachineTilePacket packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			Container container = player.containerMenu;

			if (container instanceof MachineContainer<?>) {
				if (((MachineContainer<?>) container).tile != null) {
					((MachineContainer<?>) container).tile.handleGuiPacket(packet);
				}
			}

			ctx.get().setPacketHandled(true);
		});
	}
}
