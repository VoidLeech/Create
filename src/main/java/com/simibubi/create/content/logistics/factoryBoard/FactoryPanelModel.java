package com.simibubi.create.content.logistics.factoryBoard;

import java.util.Arrays;
import java.util.function.Supplier;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock.PanelSlot;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock.PanelState;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock.PanelType;
import com.simibubi.create.foundation.model.BakedQuadHelper;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.Util;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

public class FactoryPanelModel extends ForwardingBakedModel {
	private static final MaterialFinder materialFinder = Util.make(() -> {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		return renderer == null ? null : renderer.materialFinder();
	});

	public FactoryPanelModel(BakedModel originalModel) {
		wrapped = originalModel;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState blockState, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		if (!(level.getBlockEntityRenderData(pos) instanceof FactoryPanelBlockEntity.RenderData data))
			return;

		boolean ponder = level instanceof PonderLevel;

		this.wrapped.emitBlockQuads(level, blockState, pos, randomSupplier, context);
		data.states().forEach((slot, state) -> this.emitPanel(
			level, blockState, pos, randomSupplier, context,
			slot, data.type(), state, ponder
		));
	}

	public void emitPanel(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context,
						 PanelSlot slot, PanelType type, PanelState panelState, boolean ponder) {
		PartialModel factoryPanel = panelState == PanelState.PASSIVE
			? type == PanelType.NETWORK ? AllPartialModels.FACTORY_PANEL : AllPartialModels.FACTORY_PANEL_RESTOCKER
			: type == PanelType.NETWORK ? AllPartialModels.FACTORY_PANEL_WITH_BULB
			: AllPartialModels.FACTORY_PANEL_RESTOCKER_WITH_BULB;

		float xRot = Mth.RAD_TO_DEG * FactoryPanelBlock.getXRot(state);
		float yRot = Mth.RAD_TO_DEG * FactoryPanelBlock.getYRot(state);

		context.pushTransform((quad) -> {
			int[] vertices = new int[MutableQuadView.VANILLA_QUAD_STRIDE];
			quad.toVanilla(vertices, 0);
			int[] transformedVertices = Arrays.copyOf(vertices, vertices.length);

			Vec3 quadNormal = Vec3.atLowerCornerOf(quad.lightFace().getNormal());
			quadNormal = VecHelper.rotate(quadNormal, 180, Axis.Y);
			quadNormal = VecHelper.rotate(quadNormal, xRot + 90, Axis.X);
			quadNormal = VecHelper.rotate(quadNormal, yRot, Axis.Y);

			for (int i = 0; i < vertices.length / BakedQuadHelper.VERTEX_STRIDE; i++) {
				Vec3 vertex = BakedQuadHelper.getXYZ(vertices, i);
				// fabric: normal here was never used, removed

				vertex = vertex.add(slot.xOffset * .5, 0, slot.yOffset * .5);
				vertex = VecHelper.rotateCentered(vertex, 180, Axis.Y);
				vertex = VecHelper.rotateCentered(vertex, xRot + 90, Axis.X);
				vertex = VecHelper.rotateCentered(vertex, yRot, Axis.Y);

				BakedQuadHelper.setXYZ(transformedVertices, i, vertex);
				BakedQuadHelper.setNormalXYZ(transformedVertices, i, new Vec3(0, 1, 0));
			}

			Direction newNormal = Direction.fromDelta((int) Math.round(quadNormal.x), (int) Math.round(quadNormal.y),
				(int) Math.round(quadNormal.z));

			quad.fromVanilla(transformedVertices, 0);
			quad.cullFace(newNormal);

			if (ponder && !quad.material().disableDiffuse()) {
				RenderMaterial newMaterial = materialFinder.copyFrom(quad.material()).disableDiffuse(true).find();
				quad.material(newMaterial);
			}

			return true;
		});

		factoryPanel.get().emitBlockQuads(level, state, pos, randomSupplier, context);
		context.popTransform();
	}

}
