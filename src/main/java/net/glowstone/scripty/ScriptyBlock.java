package net.glowstone.scripty;

import net.glowstone.scripty.net.ScriptyNetworkHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class ScriptyBlock extends BlockContainer {

    private final String name = "scripty_block";

    public ScriptyBlock() {
        super(new Material(MapColor.GREEN));
        setUnlocalizedName("scripty_block");
        setRegistryName("scripty", "scripty_block");
        setCreativeTab(CreativeTabs.REDSTONE);
        GameRegistry.registerWithItem(this);
        GameRegistry.registerTileEntity(ScriptyBlock.TEScriptyBlock.class, "scripty:scripty_block");
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

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEScriptyBlock();
    }

    public static class TEScriptyBlock extends TileEntity {

        private String content;
        private ScriptLanguage language;

        public TEScriptyBlock() {
            this.content = "";
            this.language = ScriptLanguage.PYTHON;
        }

        public TEScriptyBlock(String content, ScriptLanguage language) {
            this.content = content;
            this.language = language;
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            compound.setByte("Language", (byte) language.ordinal());
            compound.setString("Content", content);
            return compound;
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            setLanguage(ScriptLanguage.values()[compound.getByte("Language")]);
            setContent(compound.getString("Content"));
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public ScriptLanguage getLanguage() {
            return language;
        }

        public void setLanguage(ScriptLanguage language) {
            this.language = language;
        }

        @Override
        public boolean onlyOpsCanSetNbt() {
            return true;
        }
    }
}
