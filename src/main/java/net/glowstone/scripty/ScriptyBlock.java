package net.glowstone.scripty;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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

    public String getName() {
        return name;
    }
}
