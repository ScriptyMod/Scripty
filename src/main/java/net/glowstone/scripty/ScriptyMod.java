package net.glowstone.scripty;

import net.glowstone.scripty.gui.ScriptyGUI;
import net.glowstone.scripty.net.ScriptyNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import javax.script.ScriptEngineManager;

@Mod(modid = "scripty", version = "0.0.1")
public class ScriptyMod {

    private ScriptyBlock scriptyBlock;
    @SidedProxy(clientSide = "net.glowstone.scripty.gui.ScriptyGUI$Client", serverSide = "net.glowstone.scripty.gui.ScriptyGUI$Common")
    private static ScriptyGUI.Common gui;
    public static ScriptyMod INSTANCE;

    public static final ScriptEngineManager ENGINE_MANAGER = new ScriptEngineManager();

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        // pre-init
        INSTANCE = this;
        ScriptyNetworkHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // init
        MinecraftForge.EVENT_BUS.register(this);
        scriptyBlock = new ScriptyBlock();
        if (event.getSide() == Side.CLIENT) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            renderItem.getItemModelMesher().register(Item.getItemFromBlock(scriptyBlock), 0, new ModelResourceLocation("scripty:" + scriptyBlock.getName(), "inventory"));
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(this, gui);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(scriptyBlock);
    }
}
