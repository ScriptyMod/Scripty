package net.glowstone.scripty;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import javax.script.ScriptException;

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
        return true;
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
        private EntityPlayer owner;
        private boolean parsing;

        public TEScriptyBlock() {
            this.content = "";
            this.language = ScriptLanguage.PYTHON;
            this.parsing = false;
            this.owner = null;
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

        public boolean isParsing() {
            return parsing;
        }

        public void setParsing(boolean parsing) {
            this.parsing = parsing;
        }

        public EntityPlayer getOwner() {
            return owner;
        }

        public void setOwner(EntityPlayer owner) {
            this.owner = owner;
        }

        @Override
        public boolean onlyOpsCanSetNbt() {
            return true;
        }

        public void parse() {
            if (isParsing() || owner == null) {
                System.out.println("not parsing because no owner/already parsing");
                return;
            }
            setParsing(true);
            ListenableFuture<Object> future = owner.getServer().addScheduledTask(() -> {
                try {
                    String content = getContent();
                    ScriptLanguage language = getLanguage();
                    if (language.getEngine() == null) {
                        System.out.println("WARNING: " + language + " has no engine associated.");
                    } else {
                        language.getEngine().eval(content);
                    }
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            });
            Futures.addCallback(future, new FutureCallback<Object>() {
                @Override
                public void onSuccess(@Nullable Object result) {
                    setParsing(false);
                    if (owner != null) {
                        ScriptyNetworkHandler.sendContentMessage((EntityPlayerMP) owner, pos, content, language, parsing);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    setParsing(false);
                    if (owner != null) {
                        ScriptyNetworkHandler.sendContentMessage((EntityPlayerMP) owner, pos, content, language, parsing);
                    }
                }
            });
        }
    }
}
