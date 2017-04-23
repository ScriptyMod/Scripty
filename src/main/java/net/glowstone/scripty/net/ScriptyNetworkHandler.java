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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ScriptyNetworkHandler {

    public static SimpleNetworkWrapper WRAPPER;

    public static void init() {
        WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("scripty");
        WRAPPER.registerMessage(ScriptyPacketContent.HandlerClient.class, ScriptyPacketContent.class, 0x00, Side.CLIENT);
        WRAPPER.registerMessage(ScriptyPacketContent.HandlerServer.class, ScriptyPacketContent.class, 0x01, Side.SERVER);
        WRAPPER.registerMessage(ScriptyPacketClose.HandlerServer.class, ScriptyPacketClose.class, 0x02, Side.SERVER);
    }

    @SideOnly(Side.CLIENT)
    public static void handleContentMessage(ScriptyPacketContent content) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (!(Minecraft.getMinecraft().currentScreen instanceof ScriptyBlockGUI)) {
            player.openGui(ScriptyMod.INSTANCE, 0x0, player.world, content.getPos().getX(), content.getPos().getY(), content.getPos().getZ());
        }
        if (Minecraft.getMinecraft().currentScreen instanceof ScriptyBlockGUI) {
            ((ScriptyBlockGUI) Minecraft.getMinecraft().currentScreen).setLanguage(content.getLanguage());
            ((ScriptyBlockGUI) Minecraft.getMinecraft().currentScreen).setContent(content.getContent());
            ((ScriptyBlockGUI) Minecraft.getMinecraft().currentScreen).setPos(content.getPos());
            ((ScriptyBlockGUI) Minecraft.getMinecraft().currentScreen).setParsing(content.isParsing());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendContentUpdate(ScriptyPacketContent content) {
        WRAPPER.sendToServer(content);
    }

    @SideOnly(Side.SERVER)
    public static void handleContentUpdate(ScriptyPacketContent content, EntityPlayerMP player) {
        BlockPos pos = content.getPos();
        TileEntity entity = player.world.getTileEntity(pos);
        if (entity != null && entity instanceof ScriptyBlock.TEScriptyBlock) {
            ((ScriptyBlock.TEScriptyBlock) entity).setLanguage(content.getLanguage());
            ((ScriptyBlock.TEScriptyBlock) entity).setContent(content.getContent());
            ((ScriptyBlock.TEScriptyBlock) entity).parse();
            entity.validate();
        }
    }

    @SideOnly(Side.SERVER)
    public static void sendContentMessage(EntityPlayerMP player, BlockPos pos, String content, ScriptLanguage language, boolean parsing) {
        WRAPPER.sendTo(new ScriptyPacketContent(pos, content, language, parsing), player);
    }

    @SideOnly(Side.SERVER)
    public static void handleOpenRequest(BlockPos pos, EntityPlayerMP player) {
        TileEntity te = player.world.getTileEntity(pos);
        if (te instanceof ScriptyBlock.TEScriptyBlock) {
            ScriptyBlock.TEScriptyBlock scriptyBlock = (ScriptyBlock.TEScriptyBlock) te;
            if (scriptyBlock.getOwner() != null && scriptyBlock.getOwner().isEntityEqual(player)) {
                player.sendMessage(new TextComponentString(scriptyBlock.getOwner().getName() + " is already using this Scripty block.").setStyle(new Style().setColor(TextFormatting.RED)));
                return;
            }
            scriptyBlock.setOwner(player);
            sendContentMessage(player, pos, scriptyBlock.getContent(), scriptyBlock.getLanguage(), scriptyBlock.isParsing());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendCloseMessage(BlockPos pos) {
        WRAPPER.sendToServer(new ScriptyPacketClose(pos));
    }

    @SideOnly(Side.SERVER)
    public static void handleCloseMessage(ScriptyPacketClose message, EntityPlayerMP player) {
        BlockPos pos = message.getPos();
        TileEntity te = player.world.getTileEntity(pos);
        if (te instanceof ScriptyBlock.TEScriptyBlock) {
            ScriptyBlock.TEScriptyBlock scriptyBlock = (ScriptyBlock.TEScriptyBlock) te;
            scriptyBlock.setOwner(null);
        }
    }
}
