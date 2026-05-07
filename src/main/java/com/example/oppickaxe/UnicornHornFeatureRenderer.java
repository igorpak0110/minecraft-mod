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

public class UnicornHornFeatureRenderer extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    private static final Identifier HORN_TEXTURE =
            Identifier.of(OpPickaxeMod.MOD_ID, "textures/entity/unicorn_horn.png");

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        // Proportional horn: 4×10×4 px → 0.25×0.625×0.25 blocks
        // (about 2× the head's height, matches typical unicorn-horn proportions)
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-2f, -10f, -2f, 4, 10, 4),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 32, 32).createModel().getChild("horn");
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

        // Walk the model tree exactly as the renderer does:
        //   body.rotate() applies body pivot+rotation,
        //   head.rotate() applies head_parts pivot+rotation (incl. setAngles' yaw/pitch
        //   AND any grazing/jumping animation already applied to the field).
        // After both, we are anchored at the head_parts pivot in the head's local frame.
        body.rotate(matrices);
        head.rotate(matrices);

        // ModelPart renders in block units (Cuboid stores vertices pre-divided by 16),
        // so NO additional scale is needed here. That was the bug making the horn tiny.
        //
        // The head cube extends in head-local pixel space:
        //   y = -11 .. -6  (top..bottom of head)
        //   z = -2 .. 5    (front..back of head)
        // → block space: y = -0.6875 .. -0.375,  z = -0.125 .. 0.3125
        //
        // Place the horn just above the top of the head, at the front (forehead).
        // Translate to (0, -0.7, -0.05) in head-local block space.
        matrices.translate(0.0f, -0.7f, -0.05f);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
