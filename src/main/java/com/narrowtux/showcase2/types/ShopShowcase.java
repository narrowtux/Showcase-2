package com.narrowtux.showcase2.types;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.showcase2.ShowcasePlayer;

public class ShopShowcase extends Showcase {

	private double buyPrice = -1; //Money a player has to pay
	private double sellPrice = -1; //Money a player gets
	
	private int amount = 0; //amount of items in showcase
	private int maxamount = 0; //maximum amount of items the showcase will buy (can be lower than amount)
	
	private boolean init = false;
	
	public ShopShowcase(Block block, ItemStack type, ShowcasePlayer owner) {
		super(block, type, owner);
		
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setMaxAmount(int maxamount) {
		this.maxamount = maxamount;
	}

	public int getMaxAmount() {
		return maxamount;
	}

	@Override
	public void doSave(HashMap<String, Object> map) {
		map.put("amount", getAmount());
		map.put("buyprice", getBuyPrice());
		map.put("sellprice", getSellPrice());
		map.put("maxamount", getMaxAmount());
	}

	@Override
	public void doLoad(HashMap<String, Object> map) {
		setAmount((Integer) map.get("amount"));
		setBuyPrice((Double) map.get("buyprice"));
		setSellPrice((Double) map.get("sellprice"));
		setMaxAmount((Integer) map.get("maxamount"));
	}

	@Override
	public void onLeftClick(ShowcasePlayer player) {
		ShopAssistant assistant = new ShopAssistant(player, this);
		assistant.start();
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
		if(isOwner(player)) {
			ShopSetupAssistant assistant = new ShopSetupAssistant(player, this);
			assistant.start();
		}
	}

	@Override
	public boolean onCreate(ShowcasePlayer player) {
		if(!isInit()) {
			ShopSetupAssistant assistant = new ShopSetupAssistant(player, this);
			assistant.start();
		}
		return true;
	}

	@Override
	public boolean onRemove(ShowcasePlayer player) {
		return true;
	}

	public boolean isRemoved() {
		return Showcase.getShowcase(getItem()) == null;
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
}
