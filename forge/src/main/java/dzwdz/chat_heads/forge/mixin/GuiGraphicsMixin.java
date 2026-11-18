package dzwdz.chat_heads.forge.mixin;

import dzwdz.chat_heads.ChatHeads;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;FFIZ)I",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", shift = At.Shift.BEFORE),
        require = 1,
        allow = 1
    )
    private void onDrawStringStart(Font arg, String string, float f, float g, int j, boolean bl, CallbackInfoReturnable<Integer> cir) {
        chatheads$beginInlineHeadRender(j);
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;FFIZ)I",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", shift = At.Shift.AFTER),
        require = 1,
        allow = 1
    )
    private void onDrawStringEnd(Font arg, String string, float f, float g, int j, boolean bl, CallbackInfoReturnable<Integer> cir) {
        chatheads$endInlineHeadRender();
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;FFIZ)I",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", shift = At.Shift.BEFORE),
        require = 1,
        allow = 1
    )
    private void onDrawFormattedStart(Font arg, FormattedCharSequence arg2, float f, float g, int j, boolean bl, CallbackInfoReturnable<Integer> cir) {
        chatheads$beginInlineHeadRender(j);
    }

    @Inject(
        method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;FFIZ)I",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", shift = At.Shift.AFTER),
        require = 1,
        allow = 1
    )
    private void onDrawFormattedEnd(Font arg, FormattedCharSequence arg2, float f, float g, int j, boolean bl, CallbackInfoReturnable<Integer> cir) {
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