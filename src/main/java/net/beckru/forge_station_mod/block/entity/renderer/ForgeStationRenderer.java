package net.beckru.forge_station_mod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.beckru.forge_station_mod.block.entity.custom.ForgeStationBasicEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import com.mojang.math.Axis;  // <-- Forge 1.20.1 usa esta clase

public class ForgeStationRenderer implements BlockEntityRenderer<ForgeStationBasicEntity> {

    public ForgeStationRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ForgeStationBasicEntity entity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        ItemStack slot0 = entity.inventory.getStackInSlot(0);
        ItemStack slot1 = entity.inventory.getStackInSlot(1);

        float rotation = entity.getRenderingRotation();

        // -----------------------------
        // SLOT 0 (izquierda)
        // -----------------------------
        if (!slot0.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.30f, 1.15f, 0.5f); // izquierda
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            renderer.renderStatic(
                    slot0,
                    ItemDisplayContext.FIXED,
                    getLightLevel(entity.getLevel(), entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.getLevel(),
                    0
            );

            poseStack.popPose();
        }

        // -----------------------------
        // SLOT 1 (derecha)
        // -----------------------------
        if (!slot1.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.70f, 1.15f, 0.5f); // derecha
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            renderer.renderStatic(
                    slot1,
                    ItemDisplayContext.FIXED,
                    getLightLevel(entity.getLevel(), entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.getLevel(),
                    0
            );

            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
