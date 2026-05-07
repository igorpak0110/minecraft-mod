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

    private static final float HORSE_HEAD_BASE_PITCH = 0.5235988f;

    private final ModelPart horn;

    public UnicornHornFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
        // Smaller horn: 2x5x2 px → 0.125 x 0.3125 x 0.125 blocks (same height as the horse head)
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
        ModelPart body = accessor.getBody();
        ModelPart head = accessor.getHead();

        matrices.push();

        // Anchor to the head bone (follows grazing/jumping animations).
        body.rotate(matrices);
        head.rotate(matrices);

        // Counter the 30° base head pitch so the horn stands vertical at idle.
        // Dynamic head pitch (looking up/down, eating) still applies.
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-HORSE_HEAD_BASE_PITCH));

        // head_parts pivot is at the lower neck/throat level, not the top of the head.
        // The head cube extends UP from there: top is at y=-11 px = -0.6875 blocks (in head local).
        // Translate up well past the head top so the horn BASE sits on the forehead surface.
        // y: -1.10 → ~0.4 blocks above head top
        // z: -0.10 → just inside the front face of the head (forehead)
        matrices.translate(0.0f, -0.85f, -0.25f);

        VertexConsumer vc = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(HORN_TEXTURE));
        horn.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
