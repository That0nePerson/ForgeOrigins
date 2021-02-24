package com.thatoneperson.forgeorigin.core.init;

import com.thatoneperson.forgeorigin.ForgeOrigins;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 
			ForgeOrigins.MOD_ID);
	
	public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("infinite_power", 
			() -> new Item(new Item.Properties().group(ItemGroup.MISC)));
}
