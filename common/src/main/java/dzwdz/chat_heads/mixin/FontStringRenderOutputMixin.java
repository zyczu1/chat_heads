package dzwdz.chat_heads.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dzwdz.chat_heads.ChatHeads;
import dzwdz.chat_heads.HeadData;
import dzwdz.chat_heads.InlinePlayerHeads;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Font.StringRenderOutput.class)
public abstract class FontStringRenderOutputMixin {
    @Shadow float x;
    @Shadow float y;
    @Shadow @Final private Matrix4f pose;
    @Shadow @Final private boolean dropShadow;

    @Inject(method = "accept", at = @At("HEAD"))
    public void chatheads$renderChatHead(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir) {
        if (j == InlinePlayerHeads.GLYPH && InlinePlayerHeads.FONT.equals(style.getFont())) {
            UUID playerId = InlinePlayerHeads.getRegisteredPlayerId(style.getInsertion());

            if (!dropShadow && playerId != null && ChatHeads.guiGraphics != null) {
                PlayerInfo playerInfo = InlinePlayerHeads.getPlayerInfo(playerId);
                if (playerInfo != null) {
                    PoseStack poseStack = ChatHeads.guiGraphics.pose();
                    poseStack.pushPose();
                    try {
                        poseStack.setIdentity();
                        poseStack.mulPose(pose);
                        ChatHeads.renderInlineChatHead(ChatHeads.guiGraphics, (int) x, (int) y, playerInfo, ChatHeads.renderHeadOpacity);
                    } finally {
                        poseStack.popPose();
                    }
                }
            }

            x += ChatHeads.headWidth() - InlinePlayerHeads.BASE_ADVANCE;
        }

        if (ChatHeads.renderHeadData == HeadData.EMPTY)
            return;

        int renderIndex = Math.max(ChatHeads.renderHeadData.codePointIndex(), 0); // fallback to rendering at beginning

        if (!dropShadow && ChatHeads.renderedChars == renderIndex || dropShadow && ChatHeads.renderedShadowChars == renderIndex) {
            if (!dropShadow) {
                PoseStack poseStack = ChatHeads.guiGraphics.pose();

                poseStack.pushPose();
                poseStack.setIdentity();
                poseStack.mulPose(pose);

                ChatHeads.renderChatHead(ChatHeads.guiGraphics, (int) x + 1, (int) y, ChatHeads.renderHeadData.playerInfo(), ChatHeads.renderHeadOpacity);

                poseStack.popPose();
            }

            x += ChatHeads.headWidth();
        }

        if (!dropShadow) {
            ChatHeads.renderedChars++;
        } else {
            ChatHeads.renderedShadowChars++;
        }
    }
}
