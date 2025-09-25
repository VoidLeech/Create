package com.simibubi.create.foundation.utility.fabric;

import dev.engine_room.flywheel.lib.model.baked.VirtualBlockGetter;
import net.minecraft.world.level.BlockGetter;

/**
 * Fabric parallel to the forge-specific class of the same name provided by Ponder.
 */
public final class VirtualRenderHelper {
	private static final ThreadLocal<Boolean> forcedVirtualState = new ThreadLocal<>();

	private VirtualRenderHelper() {
	}

	public static boolean isVirtual(BlockGetter level) {
		Boolean forced = forcedVirtualState.get();
		return forced != null ? forced : level instanceof VirtualBlockGetter;
	}

	// gross hack required due to a lack of ModelData.
	public static void withForcedVirtualState(boolean virtual, Runnable runnable) {
		try {
			forcedVirtualState.set(virtual);
			runnable.run();
		} finally {
			//noinspection ThreadLocalSetWithNull - remove is more expensive, this won't run on many threads
			forcedVirtualState.set(null);
		}
	}
}
