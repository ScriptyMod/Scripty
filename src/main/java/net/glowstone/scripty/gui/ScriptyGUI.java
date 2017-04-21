package net.glowstone.scripty.gui;

import net.glowstone.scripty.ScriptyBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ScriptyGUI {

    public static class Common implements IGuiHandler {
        @Nullable
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            switch (ID) {
                case 0x0:
                    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                    if (te instanceof ScriptyBlock.TEScriptyBlock) {
                        ScriptyBlock.TEScriptyBlock scriptyBlock = (ScriptyBlock.TEScriptyBlock) te;
                        // open gui
                        ScriptyBlockGUI gui = new ScriptyBlockGUI();
                        gui.setPos(te.getPos());
                        gui.setLanguage(scriptyBlock.getLanguage());
                        gui.setContent(scriptyBlock.getContent());
                        return gui;
                    }
            }
            return null;
        }

        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }
    }

    public static class Client extends Common {
        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return super.getServerGuiElement(ID, player, world, x, y, z);
        }
    }
}
