package dzwdz.chat_heads.fabric.mixin;

import dzwdz.chat_heads.ChatHeads;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiGraphics.class, remap = true)
public abstract class GuiGraphicsMixin {

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", shift = At.Shift.BEFORE),
        require = 1,
        allow = 1
    )
    private void onDrawStringStart(Font font, String text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        chatheads$beginInlineHeadRender(color);
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", shift = At.Shift.AFTER),
        require = 1,
        allow = 1
    )
    private void onDrawStringEnd(Font font, String text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        chatheads$endInlineHeadRender();
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", shift = At.Shift.BEFORE),
        require = 1,
        allow = 1
    )
    private void onDrawFormattedStart(Font font, FormattedCharSequence text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        chatheads$beginInlineHeadRender(color);
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", shift = At.Shift.AFTER),
        require = 1,
        allow = 1
    )
    private void onDrawFormattedEnd(Font font, FormattedCharSequence text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        chatheads$endInlineHeadRender();
    }

    @Unique
    private void chatheads$beginInlineHeadRender(int color) {
        ChatHeads.guiGraphics = (GuiGraphics) (Object) this;
        ChatHeads.renderHeadOpacity = ((color >>> 24) & 0xFF) / 255.0F;
    }

    @Unique
    private void chatheads$endInlineHeadRender() {
        GuiGraphics current = (GuiGraphics) (Object) this;
        if (ChatHeads.guiGraphics == current) {
            ChatHeads.guiGraphics = null;
        }
    }

}