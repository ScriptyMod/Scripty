package net.glowstone.scripty;

import net.glowstone.scripty.net.ScriptyNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ScriptyBlock extends Block {

    private final String name = "scripty_block";

    public ScriptyBlock() {
        super(new Material(MapColor.GREEN));
        setUnlocalizedName("scripty_block");
        setRegistryName("scripty", "scripty_block");
        setCreativeTab(CreativeTabs.REDSTONE);
        GameRegistry.registerWithItem(this);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (!playerIn.canUseCommandBlock()) {
            return false;
        }
        ScriptyNetworkHandler.handleOpenRequest(pos, (EntityPlayerMP) playerIn);
        return false;
    }

    public String getName() {
        return name;
    }
}
