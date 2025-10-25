package com.simibubi.create.infrastructure.fabric;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @see SimpleEntityVisualFactory
 */
@FunctionalInterface
public interface SimpleBlockEntityVisualFactory<T extends BlockEntity> {
	BlockEntityVisual<? super T> create(VisualizationContext ctx, T entity, float partialTicks);
}
