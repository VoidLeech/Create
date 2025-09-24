package com.simibubi.create.infrastructure.data;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileTypes;
import com.simibubi.create.infrastructure.worldgen.AllConfiguredFeatures;
import com.simibubi.create.infrastructure.worldgen.AllPlacedFeatures;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

// fabric: reworked to use the fabric API
public class GeneratedEntriesProvider extends FabricDynamicRegistryProvider {
	public GeneratedEntriesProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		// make sure all registries below are mirrored here
		entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
		entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
		entries.addAll(registries.lookupOrThrow(CreateRegistries.POTATO_PROJECTILE_TYPE));
	}

	// fabric: this must be used in the entrypoint, moved to a method
	public static RegistrySetBuilder addBootstraps(RegistrySetBuilder builder) {
		return builder.add(Registries.DAMAGE_TYPE, AllDamageTypes::bootstrap)
				.add(Registries.CONFIGURED_FEATURE, AllConfiguredFeatures::bootstrap)
				.add(Registries.PLACED_FEATURE, AllPlacedFeatures::bootstrap)
				.add(CreateRegistries.POTATO_PROJECTILE_TYPE, AllPotatoProjectileTypes::bootstrap);
		// fabric: biome modifiers are not a registry, remove
	}

	@Override
	public String getName() {
		return "Create's Generated Registry Entries";
	}
}
