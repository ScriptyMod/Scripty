package net.glowstone.scripty;

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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "scripty", version = "0.0.1")
public class ScriptyMod {

    private ScriptyBlock scriptyBlock;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        // pre-init
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
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(scriptyBlock);
    }
}
