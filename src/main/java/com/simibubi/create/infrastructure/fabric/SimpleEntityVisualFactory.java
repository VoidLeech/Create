package com.simibubi.create.infrastructure.fabric;

import dev.engine_room.flywheel.api.visual.EntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.engine_room.flywheel.lib.visualization.SimpleEntityVisualizer;
import net.minecraft.world.entity.Entity;

/**
 * This class (and {@link SimpleBlockEntityVisualFactory} are a hacky workaround to avoid classloading issues.
 * On Forge, Flywheel loads despite being a client-only mod. On Fabric, it's entirely skipped.
 * This means that {@link SimpleEntityVisualizer.Factory} and {@link SimpleBlockEntityVisualizer.Factory} cannot
 * be referenced safely, and these classes must be used instead.
 */
@FunctionalInterface
public interface SimpleEntityVisualFactory<T extends Entity> {
	EntityVisual<? super T> create(VisualizationContext ctx, T entity, float partialTicks);
}
