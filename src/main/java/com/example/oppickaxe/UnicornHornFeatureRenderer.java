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

    private static final float HORSE_HEAD_BASE_PITCH = (float) (Math.PI / 6); // 30°

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        // 2x5x2 px → 0.125 × 0.3125 × 0.125 blocks
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1f, -5f, -1f, 2, 5, 2),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 16, 16).createModel().getChild("horn");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, HorseEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) return;

        HorseEntityModelAccessor accessor = (HorseEntityModelAccessor) this.getContextModel();
        ModelPart head = accessor.getHead();

        matrices.push();

        // head_parts is a CHILD OF ROOT (sibling of body), not nested under body.
        // Only call head.rotate() — calling body.rotate() first was double-compounding transforms.
        head.rotate(matrices);

        // Cancel head_parts's baked 30° pitch so the horn stands vertical at idle
        // (dynamic pitch from setAngles still applies).
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-HORSE_HEAD_BASE_PITCH));

        // Forehead = top-front corner of head cube = (0, -11px, -2px) = (0, -0.6875, -0.125) blocks
        // Place horn base just slightly above the head's top surface and at the front.
        matrices.translate(0.0f, -0.70f, -0.10f);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
