package com.simibubi.create.content.logistics.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simibubi.create.content.logistics.filter.AttributeFilterMenu.WhitelistMode;
import com.simibubi.create.content.logistics.filter.FilterItemStack.AttributeFilterItemStack;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.attributes.InTagAttribute;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet.ListBacked;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class AttributeFilterItem extends FilterItem {
	protected AttributeFilterItem(Properties properties) {
		super(properties);
	}

	@Override
	public List<Component> makeSummary(ItemStack filter) {
		if (!filter.hasTag()) return Collections.emptyList();

		List<Component> list = new ArrayList<>();

		WhitelistMode whitelistMode = WhitelistMode.values()[filter.getOrCreateTag()
			.getInt("WhitelistMode")];
		list.add((whitelistMode == WhitelistMode.WHITELIST_CONJ
			? CreateLang.translateDirect("gui.attribute_filter.allow_list_conjunctive")
			: whitelistMode == WhitelistMode.WHITELIST_DISJ
			? CreateLang.translateDirect("gui.attribute_filter.allow_list_disjunctive")
			: CreateLang.translateDirect("gui.attribute_filter.deny_list")).withStyle(ChatFormatting.GOLD));

		int count = 0;
		ListTag attributes = filter.getOrCreateTag()
			.getList("MatchedAttributes", Tag.TAG_COMPOUND);
		for (Tag inbt : attributes) {
			CompoundTag compound = (CompoundTag) inbt;
			ItemAttribute attribute = ItemAttribute.loadStatic(compound);
			if (attribute == null)
				continue;
			boolean inverted = compound.getBoolean("Inverted");
			if (count > 3) {
				list.add(Component.literal("- ...")
					.withStyle(ChatFormatting.DARK_GRAY));
				break;
			}
			list.add(Component.literal("- ")
				.append(attribute.format(inverted)));
			count++;
		}

		if (count == 0)
			return Collections.emptyList();

		return list;
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return AttributeFilterMenu.create(id, inv, player.getMainHandItem());
	}

	@Override
	public FilterItemStack makeStackWrapper(ItemStack filter) {
		return new AttributeFilterItemStack(filter);
	}

	@Override
	public ItemStack[] getFilterItems(ItemStack itemStack) {
		CompoundTag tag = itemStack.getOrCreateTag();

		WhitelistMode whitelistMode = WhitelistMode.values()[tag.getInt("WhitelistMode")];
		ListTag attributes = tag.getList("MatchedAttributes", net.minecraft.nbt.Tag.TAG_COMPOUND);

		if (whitelistMode == WhitelistMode.WHITELIST_DISJ && attributes.size() == 1) {
			ItemAttribute fromNBT = ItemAttribute.loadStatic((CompoundTag) attributes.get(0));
			if (fromNBT instanceof InTagAttribute inTag) {
				return BuiltInRegistries.ITEM.getTag(inTag.tag).stream()
					.flatMap(ListBacked::stream)
					.map(Holder::value)
					.map(ItemStack::new)
					.toArray(ItemStack[]::new);
			}
		}
		return new ItemStack[0];
	}
}
