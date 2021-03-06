package com.tommytony.war.structure;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.tommytony.war.Warzone;
import com.tommytony.war.volume.BlockInfo;
import com.tommytony.war.volume.Volume;

/**
 *
 * @author tommytony
 *
 */
public class Bomb {
	private Location location;
	private Volume volume;

	private final String name;
	private Warzone warzone;
	private Player capturer;

	public Bomb(String name, Warzone warzone, Location location) {
		this.name = name;
		this.location = location;
		this.warzone = warzone;
		this.volume = new Volume("bomb-" + name, warzone.getWorld());
		this.setLocation(location);
	}

	public void addBombBlocks() {
		// make air (old two-high above floor)
		Volume airGap = new Volume("airgap", this.warzone.getWorld());
		airGap.setCornerOne(new BlockInfo(
				this.volume.getCornerOne().getX(), 
				this.volume.getCornerOne().getY() + 1, 
				this.volume.getCornerOne().getZ(),
				0,
				(byte)0));
		airGap.setCornerTwo(new BlockInfo(
				this.volume.getCornerTwo().getX(), 
				this.volume.getCornerOne().getY() + 3, 
				this.volume.getCornerTwo().getZ(),
				0,
				(byte)0));
		airGap.setToMaterial(Material.AIR);

		int x = this.location.getBlockX();
		int y = this.location.getBlockY();
		int z = this.location.getBlockZ();
		
		Material main = Material.getMaterial(this.warzone.getWarzoneMaterials().getMainId());
		byte mainData = this.warzone.getWarzoneMaterials().getMainData();
		Material stand = Material.getMaterial(this.warzone.getWarzoneMaterials().getStandId());
		byte standData = this.warzone.getWarzoneMaterials().getStandData();
		Material light = Material.getMaterial(this.warzone.getWarzoneMaterials().getLightId());
		byte lightData = this.warzone.getWarzoneMaterials().getLightData();

		// center
		Block current = this.warzone.getWorld().getBlockAt(x, y - 1, z);
		current.setType(main);
		current.setData(mainData);

		// inner ring
		current = this.warzone.getWorld().getBlockAt(x + 1, y - 1, z + 1);
		current.setType(light);
		current.setData(lightData);
		current = this.warzone.getWorld().getBlockAt(x + 1, y - 1, z);
		current.setType(main);
		current.setData(mainData);
		current = this.warzone.getWorld().getBlockAt(x + 1, y - 1, z - 1);
		current.setType(main);
		current.setData(mainData);

		current = this.warzone.getWorld().getBlockAt(x, y - 1, z + 1);
		current.setType(main);
		current.setData(mainData);
		current = this.warzone.getWorld().getBlockAt(x, y - 1, z);
		current.setType(light);
		current.setData(lightData);
		current = this.warzone.getWorld().getBlockAt(x, y - 1, z - 1);
		current.setType(main);
		current.setData(mainData);

		current = this.warzone.getWorld().getBlockAt(x - 1, y - 1, z + 1);
		current.setType(main);
		current.setData(mainData);
		current = this.warzone.getWorld().getBlockAt(x - 1, y - 1, z);
		current.setType(main);
		current.setData(mainData);
		current = this.warzone.getWorld().getBlockAt(x - 1, y - 1, z - 1);
		current.setType(light);
		current.setData(lightData);

		// block holder
		current = this.warzone.getWorld().getBlockAt(x, y, z);
		current.setType(stand);
		current.setData(standData);
		Block tntBlock = this.warzone.getWorld().getBlockAt(x, y + 1, z);
		tntBlock.setType(Material.TNT);
	}

	public boolean isBombBlock(Location otherLocation) {
		int x = this.location.getBlockX();
		int y = this.location.getBlockY() + 1;
		int z = this.location.getBlockZ();
		int otherX = otherLocation.getBlockX();
		int otherY = otherLocation.getBlockY();
		int otherZ = otherLocation.getBlockZ();
		
		return x == otherX
			&& y == otherY
			&& z == otherZ;
	}

	public void capture(Player capturer) {
		this.capturer = capturer;
	}
	
	public boolean isCaptured() {
		return this.capturer != null;
	}

	public void uncapture() {
		this.capturer = null;
	}

	public Location getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public void setLocation(Location location) {
		Block locationBlock = this.warzone.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		this.volume.setCornerOne(locationBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST, 1).getRelative(BlockFace.SOUTH, 1));
		this.volume.setCornerTwo(locationBlock.getRelative(BlockFace.UP, 2).getRelative(BlockFace.WEST, 1).getRelative(BlockFace.NORTH, 1));
		this.volume.saveBlocks();
		this.location = location;
		this.addBombBlocks();
	}

	public Volume getVolume() {
		return this.volume;
	}

	public void setVolume(Volume newVolume) {
		this.volume = newVolume;

	}
}
