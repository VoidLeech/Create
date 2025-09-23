package com.simibubi.create.foundation.utility.fabric;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.simibubi.create.foundation.utility.fabric.CustomRenderTypeGeometryLoader.Geometry;

import io.github.fabricators_of_create.porting_lib.models.CustomBlendModeModel;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;

import io.github.fabricators_of_create.porting_lib.models.util.RenderTypeUtil;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.function.Function;

public enum CustomRenderTypeGeometryLoader implements IGeometryLoader<Geometry> {
	INSTANCE;

	@Override
	public Geometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		if (RendererAccess.INSTANCE.getRenderer() == null) {
			throw new JsonParseException("Cannot use RenderMaterial without FRAPI.");
		}

		String typeName = GsonHelper.getAsString(json, "render_type");
		BlendMode blendMode = BlendMode.fromRenderLayer(RenderTypeUtil.get(new ResourceLocation(typeName)));
		JsonObject wrappedJson = json.getAsJsonObject("wrapped");
		BlockModel wrapped = context.deserialize(wrappedJson, BlockModel.class);
		return new Geometry(blendMode, wrapped);
	}

	public record Geometry(BlendMode mode, BlockModel wrapped) implements IUnbakedGeometry<Geometry> {
		@Override
		public BakedModel bake(BlockModel context, ModelBaker bakery, Function<Material, TextureAtlasSprite> textures, ModelState state, ItemOverrides overrides, ResourceLocation id, boolean b) {
			BakedModel wrapped = this.wrapped.bake(bakery, context, textures, state, id, b);
			return new CustomBlendModeModel(wrapped, this.mode);
		}
	}
}
