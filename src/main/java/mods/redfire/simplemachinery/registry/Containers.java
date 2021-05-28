package mods.redfire.simplemachinery.registry;

import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.tileentities.sieve.SieveContainer;
import mods.redfire.simplemachinery.tileentities.sieve.SieveTile;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableContainer;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Containers {
	private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleMachinery.MODID);

	public static final RegistryObject<ContainerType<SieveContainer>> SIEVE_CONTAINER = CONTAINERS.register(Names.SIEVE, () -> IForgeContainerType.create(
			(id, inv, data) -> {
				World world = inv.player.level;
				BlockPos pos = data.readBlockPos();

				TileEntity tile = world.getBlockEntity(pos);
				return tile instanceof SieveTile ? new SieveContainer(id, world, pos, inv, (SieveTile) tile) : new SieveContainer(id, world, pos, inv);
			}
	));
	public static final RegistryObject<ContainerType<TurntableContainer>> TURNTABLE_CONTAINER = CONTAINERS.register(Names.TURNTABLE, () -> IForgeContainerType.create(
			(id, inv, data) -> {
				World world = inv.player.level;
				BlockPos pos = data.readBlockPos();

				TileEntity tile = world.getBlockEntity(pos);
				return tile instanceof TurntableTile ? new TurntableContainer(id, world, pos, inv, (TurntableTile) tile) : new TurntableContainer(id, world, pos, inv);
			}
	));

	public static void register(IEventBus bus) {
		CONTAINERS.register(bus);
	}
}
