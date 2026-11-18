package dzwdz.chat_heads.mixin;

import dzwdz.chat_heads.InlinePlayerHeads;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkullBlockEntity.class)
public class MixinSkullBlockEntity {

    @Inject(
        method = "clear",
        at = @At("HEAD")
    )
    private static void onClear(CallbackInfo ci) {
        InlinePlayerHeads.invalidateAllPlayers();
    }

}
