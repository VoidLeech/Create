package com.simibubi.create.foundation.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.AllTags.AllFluidTags;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
	public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
		super(clientLevel, gameProfile);
	}

	@Inject(method = "getWaterVision", at = @At("HEAD"), cancellable = true)
	private void noFogFadeInCreateFluids(CallbackInfoReturnable<Float> cir) {
		if (this.isEyeInFluid(AllFluidTags.CREATE_FLUIDS.tag)) {
			cir.setReturnValue(1f);
		}
	}
}
