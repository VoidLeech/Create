package com.simibubi.create.foundation.mixin.fabric;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllTags.AllFluidTags;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
	@WrapOperation(
		method = "renderScreenEffect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"
		)
	)
	private static boolean improveVisibilityInCreateFluids(LocalPlayer player, TagKey<Fluid> water, Operation<Boolean> original) {
		boolean inWater = original.call(player, water);
		if (!inWater) {
			return false;
		}

		return !original.call(player, AllFluidTags.CREATE_FLUIDS.tag);
	}
}
