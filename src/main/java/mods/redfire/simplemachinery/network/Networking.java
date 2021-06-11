/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.network;

import mods.redfire.simplemachinery.SimpleMachinery;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
	private static SimpleChannel INSTANCE;
	private static int ID = 0;

	private static int nextID() {
		return ID++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SimpleMachinery.MODID, "network"),
				() -> "1.0",
				s -> true,
				s -> true);

		INSTANCE.messageBuilder(MachineTilePacket.class, nextID())
				.encoder(MachineTilePacket::encode)
				.decoder(MachineTilePacket::decode)
				.consumer(MachineTilePacket::consume)
				.add();
	}

	public static void sendToClient(MachineTilePacket packet, ServerPlayerEntity player) {
		INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendToServer(MachineTilePacket packet) {
		INSTANCE.sendToServer(packet);
	}
}
