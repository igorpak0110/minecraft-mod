package com.example.oppickaxe;

import com.example.oppickaxe.mixin.HorseEntityModelAccessor;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class UnicornHornFeatureRenderer extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    private static final Identifier HORN_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/entity/unicorn_horn.png");

    /** The horse head model has a 30° (0.5236 rad) downward pitch baked into head_parts.
     *  We subtract that here so the horn renders vertical at idle. Dynamic head pitch
     *  (eating, looking up) still applies because we only undo the BASE pitch. */
    private static final float HORSE_HEAD_BASE_PITCH = 0.5235988f;

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        // 2×6×2 px → 0.125 × 0.375 × 0.125 blocks (slim, ~head height)
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1f, -6f, -1f, 2, 6, 2),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 16, 16).createModel().getChild("horn");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, HorseEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) return;

        HorseEntityModelAccessor accessor = (HorseEntityModelAccessor) this.getContextModel();
        ModelPart body = accessor.getBody();
        ModelPart head = accessor.getHead();

        matrices.push();

        // Anchor to the head bone so we follow grazing/jumping animations
        body.rotate(matrices);
        head.rotate(matrices);

        // Counter the 30° base pitch baked into head_parts.
        // Dynamic head pitch (head.pitch - base) still rotates the horn naturally.
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-HORSE_HEAD_BASE_PITCH));

        // Position on the forehead, just above the head's top surface.
        // Head local block space: top y=-0.6875, front z=-0.125, pivot at z=0.
        matrices.translate(0.0f, -0.55f, -0.20f);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
