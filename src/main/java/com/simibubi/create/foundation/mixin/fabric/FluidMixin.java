package com.simibubi.create.foundation.mixin.fabric;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.AllFluids;

import net.minecraft.world.level.material.Fluid;

import io.github.fabricators_of_create.porting_lib.fluids.FluidType;

@Mixin(Fluid.class)
public class FluidMixin {
	@Dynamic("getFluidType is injected by Porting Lib and inherited from FluidExtension")
	@Inject(method = "getFluidType", at = @At("HEAD"), cancellable = true)
	private void handleCreateFluids(CallbackInfoReturnable<FluidType> cir) {
		FluidType type = AllFluids.getTypeOf((Fluid) (Object) this);
		if (type != null) {
			cir.setReturnValue(type);
		}
	}
}
