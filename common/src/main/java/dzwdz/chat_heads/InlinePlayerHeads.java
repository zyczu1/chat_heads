package dzwdz.chat_heads;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class InlinePlayerHeads {

    public static final int GLYPH = 0xE000;
    public static final int BASE_ADVANCE = 8;

    public static final ResourceLocation FONT = ResourceLocation.fromNamespaceAndPath(ChatHeads.MOD_ID, "inline_head");

    private static final Map<UUID, CompletableFuture<Optional<PlayerInfo>>> PLAYER_INFOS = new ConcurrentHashMap<>();

    private InlinePlayerHeads() {}

    public static Component playerHead(UUID playerId) {
        return Component.literal(String.valueOf((char) GLYPH))
            .setStyle(Style.EMPTY.withFont(FONT).withInsertion(playerId.toString()));
    }

    public static UUID getRegisteredPlayerId(String insertion) {
        if (insertion == null) return null;
        try {
            return UUID.fromString(insertion);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static PlayerInfo getPlayerInfo(UUID playerId) {
        Minecraft minecraft = Minecraft.getInstance();

        ClientPacketListener connection = minecraft.getConnection();
        if (connection != null) {
            PlayerInfo info = connection.getPlayerInfo(playerId);
            if (info != null) return info;
        }


        CompletableFuture<Optional<PlayerInfo>> lookup = PLAYER_INFOS.computeIfAbsent(playerId, id -> CompletableFuture.supplyAsync(() -> {
            var profileResult = minecraft.getMinecraftSessionService().fetchProfile(id, true);
            return profileResult == null ? null : profileResult.profile();
        }).thenApplyAsync(profile -> Optional.ofNullable(profile).map(resolvedProfile -> new PlayerInfo(resolvedProfile, false)), minecraft).exceptionally(error -> {
            ChatHeads.LOGGER.warn("Failed to resolve profile for inline chat head {}", id, error);
            return Optional.empty();
        }));

        return lookup.getNow(Optional.empty()).orElse(null);
    }

    public static void invalidatePlayerInfo(UUID playerId) {
        PLAYER_INFOS.remove(playerId);
    }

    public static void invalidateAllPlayers() {
        PLAYER_INFOS.clear();
    }

}
