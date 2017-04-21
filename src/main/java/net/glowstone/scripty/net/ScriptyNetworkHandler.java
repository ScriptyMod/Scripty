package net.glowstone.scripty.net;

import net.glowstone.scripty.ScriptLanguage;
import net.glowstone.scripty.ScriptyBlock;
import net.glowstone.scripty.ScriptyMod;
import net.glowstone.scripty.gui.ScriptyBlockGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
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
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        player.openGui(ScriptyMod.INSTANCE, 0x0, player.world, content.getPos().getX(), content.getPos().getY(), content.getPos().getZ());
        if (Minecraft.getMinecraft().currentScreen instanceof ScriptyBlockGUI) {
            ((ScriptyBlockGUI) Minecraft.getMinecraft().currentScreen).setLanguage(content.getLanguage());
        }
        player.sendChatMessage("content: \'" + content.getContent() + "\' of language " + content.getLanguage());
    }

    @SideOnly(Side.SERVER)
    public static void sendContentMessage(EntityPlayerMP player, BlockPos pos, String content, ScriptLanguage language) {
        WRAPPER.sendTo(new ScriptyPacketContent(pos, content, language), player);
    }

    @SideOnly(Side.SERVER)
    public static void handleOpenRequest(BlockPos pos, EntityPlayerMP player) {
        TileEntity te = player.world.getTileEntity(pos);
        if (te instanceof ScriptyBlock.TEScriptyBlock) {
            ScriptyBlock.TEScriptyBlock scriptyBlock = (ScriptyBlock.TEScriptyBlock) te;
            sendContentMessage(player, pos, scriptyBlock.getContent(), scriptyBlock.getLanguage());
        }
    }
}
