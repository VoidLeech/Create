package com.simibubi.create.api.data.recipe;

import com.simibubi.create.AllRecipeTypes;

import net.minecraft.data.PackOutput;

/**
 * The base class for Milling recipe generation.
 * Addons should extend this and use the {@link ProcessingRecipeGen#create} methods
 * or the helper methods contained in this class to make recipes.
 * For an example of how you might do this, see Create's implementation: {@link com.simibubi.create.foundation.data.recipe.CreateMillingRecipeGen}.
 * Needs to be added to a registered recipe provider to do anything, see {@link com.simibubi.create.foundation.data.recipe.CreateRecipeProvider}
 */
public abstract class MillingRecipeGen extends ProcessingRecipeGen {
	protected GeneratedRecipe moddedSandstone(DatagenMod mod, String name) {
		String sandstone = name + "_sandstone";
		return create(mod.recipeId(sandstone), b -> b.duration(150)
			.require(mod, sandstone)
			.output(mod, name + "_sand")
			.whenModLoaded(mod.getId()));
	}

	public MillingRecipeGen(PackOutput output, String defaultNamespace) {
		super(output, defaultNamespace);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.MILLING;
	}
}
