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

public class PacketScreen {
	protected BlockPos pos;
	public int energy;
	public List<FluidStack> fluids = new ArrayList<>();

	public PacketScreen() {}

	public PacketScreen(BlockPos pos, int energy, MachineFluidInventory tankInventory){
		this.pos = pos;
		this.energy = energy;
		for (int i = 0; i < tankInventory.getTanks(); i++) {
			fluids.add(tankInventory.get(i));
		}
	}

	public static void encode(PacketScreen packet, PacketBuffer buffer){
		buffer.writeBlockPos(packet.pos);
		buffer.writeInt(packet.energy);
		buffer.writeInt(packet.fluids.size());
		for (FluidStack fluid : packet.fluids) {
			buffer.writeFluidStack(fluid);
		}
	}

	public static PacketScreen decode(PacketBuffer buffer){
		PacketScreen packet = new PacketScreen();
		packet.pos = buffer.readBlockPos();
		packet.energy = buffer.readInt();
		int fluidTanks = buffer.readInt();

		for (int i = 0; i < fluidTanks; i++) {
			packet.fluids.add(buffer.readFluidStack());
		}

		return packet;
	}

	public static void consume(PacketScreen packet, Supplier<NetworkEvent.Context> ctx) {
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
