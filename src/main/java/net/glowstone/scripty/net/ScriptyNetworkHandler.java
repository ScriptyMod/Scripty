package net.glowstone.scripty.net;

import net.glowstone.scripty.ScriptLanguage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ScriptyNetworkHandler {

    public static SimpleNetworkWrapper WRAPPER;

    public static void init() {
        WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("scripty");
        WRAPPER.registerMessage(ScriptyPacketContent.Handler.class, ScriptyPacketContent.class, 0x00, Side.CLIENT);
    }

    @SideOnly(Side.CLIENT)
    public static void handleContentMessage(ScriptyPacketContent content) {
        // open gui with content
        Minecraft.getMinecraft().player.sendChatMessage("content: \'" + content.getContent() + "\' of language " + content.getLanguage());
    }

    @SideOnly(Side.SERVER)
    public static void sendContentMessage(EntityPlayerMP player, String content, ScriptLanguage language) {
        WRAPPER.sendTo(new ScriptyPacketContent(content, language), player);
    }

    @SideOnly(Side.SERVER)
    public static void handleOpenRequest(BlockPos pos, EntityPlayerMP player) {
        sendContentMessage(player, "server.broadcast(\"hello!\");", ScriptLanguage.JAVASCRIPT);
    }
}
