package com.simibubi.create.foundation.recipe.trie;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.recipe.RecipeFinder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

public class RecipeTrieFinder {
    private static final Cache<Object, RecipeTrie<?>> CACHED_TRIES = CacheBuilder.newBuilder().build();

    public static RecipeTrie<?> get(@NotNull Object cacheKey, Level world, Predicate<Recipe<?>> conditions) throws ExecutionException {
        return CACHED_TRIES.get(cacheKey, () -> {
            List<Recipe<?>> list = RecipeFinder.get(cacheKey, world, conditions);

            RecipeTrie.Builder<Recipe<?>> builder = RecipeTrie.builder();
            for (Recipe<?> recipe : list) {
                builder.insert(recipe);
            }

            return builder.build();
        });
    }

	// fabric: need to implement IdentifiableResourceReloadListener
	public enum Listener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
		INSTANCE;

		public static final ResourceLocation ID = Create.asResource("recipe_trie_finder");

		@Override
		public ResourceLocation getFabricId() {
			return ID;
		}

		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			CACHED_TRIES.invalidateAll();
		}
	}
}
