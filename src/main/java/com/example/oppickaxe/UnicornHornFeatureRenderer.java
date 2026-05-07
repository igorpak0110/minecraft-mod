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
import net.minecraft.client.render.entity.model.AbstractHorseEntityModel;
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

        // Build a simple horn: 2×12×2 pixels, UV on 16×16 sheet
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
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

        // Cast to AbstractHorseEntityModel to access the body and head_parts model parts
        // (made accessible via oppickaxe.accesswidener)
        AbstractHorseEntityModel<HorseEntity> horseModel =
                (AbstractHorseEntityModel<HorseEntity>) this.getContextModel();

        matrices.push();

        // Walk the model tree exactly as the renderer does, so the horn follows head rotation.
        // body.rotate() applies body pivot (0, 11, 5 px) relative to model root.
        // head.rotate() applies head_parts pivot (0, 4, -12 px) relative to body,
        // PLUS the dynamic yaw/pitch set by setAngles() (includes 30° base pitch).
        horseModel.body.rotate(matrices);
        horseModel.head.rotate(matrices);

        // We are now in head_parts local space (block units, since ModelPart.rotate()
        // already divides pivots by 16). Scale to pixel space for rendering.
        float s = 1.0f / 16.0f;
        matrices.scale(s, s, s);

        // Position horn at the forehead:
        // head cube spans y=-11 to y=-6 (top at y=-11), front at z=-2.
        // Place horn base at the top-front of the head.
        matrices.translate(0.0f, -11.0f, -2.0f);

        // Horn cuboid: base at y=0, tip at y=-12 (points upward in model space = visually up).
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
