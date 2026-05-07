package com.example.oppickaxe;

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

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        // Slim horn: 2×12×2 px = 0.125×0.75×0.125 blocks after scale
        root.addChild("horn",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1f, -12f, -1f, 2, 12, 2),
                ModelTransform.NONE);
        this.horn = TexturedModelData.of(modelData, 16, 16).createModel().getChild("horn");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, HorseEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) return;

        matrices.push();

        // Origin (0,0,0) is at the horse's back. Translate to the forehead:
        //   Y: -0.30 → up to head top (negative Y = up after the LivingEntityRenderer Y-flip)
        //   Z: -0.70 → forward toward the snout (-Z is the horse's forward direction)
        matrices.translate(0.0f, -0.30f, -0.70f);

        // Rotate with the head so the horn follows where the unicorn looks.
        // headYaw / headPitch arrive in degrees.
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(headPitch));

        // Scale from block units to model-pixel units for the horn cuboid
        float s = 1.0f / 16.0f;
        matrices.scale(s, s, s);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
