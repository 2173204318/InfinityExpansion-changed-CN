package io.github.mooy1.infinityexpansion.implementation.storage;

import io.github.mooy1.infinityexpansion.implementation.template.Machine;
import io.github.mooy1.infinityexpansion.lists.Categories;
import io.github.mooy1.infinityexpansion.lists.Items;
import io.github.mooy1.infinityexpansion.utils.LocationUtils;
import io.github.mooy1.infinityexpansion.utils.PresetUtils;
import io.github.mooy1.infinityexpansion.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Allows you to view the status of up to 18 storage units at once
 *
 * @author Mooy1
 */
public class StorageNetworkViewer extends Machine {

    public static final int RANGE = 32;
    public static final int MAX = 18;
    private static final int STATUS_SLOT = 4;
    private static final String[] CONNECTABLE_IDS = {
            "STORAGE_NETWORK_VIEWER",
            "STORAGE_DUCT",
            "BASIC_STORAGE",
            "ADVANCED_STORAGE",
            "REINFORCED_STORAGE",
            "VOID_STORAGE",
            "INFINITY_STORAGE",
    };
    private static final Material[] CONNECTABLE_MATS = {
            Items.STORAGE_NETWORK_VIEWER.getType(),
            Items.STORAGE_DUCT.getType(),
            Items.BASIC_STORAGE.getType(),
            Items.ADVANCED_STORAGE.getType(),
            Items.REINFORCED_STORAGE.getType(),
            Items.VOID_STORAGE.getType(),
            Items.INFINITY_STORAGE.getType(),
    };
    private static final String[] UNITS = {
            "INFINITY_STORAGE",
            "VOID_STORAGE",
            "REINFORCED_STORAGE",
            "ADVANCED_STORAGE",
            "BASIC_STORAGE",
    };

    public StorageNetworkViewer() {
        super(Categories.STORAGE_TRANSPORT, Items.STORAGE_NETWORK_VIEWER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                Items.STORAGE_DUCT, Items.MACHINE_CIRCUIT, Items.STORAGE_DUCT,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CORE, Items.MACHINE_CIRCUIT,
                Items.STORAGE_DUCT, Items.MACHINE_CIRCUIT, Items.STORAGE_DUCT,
        });
    }

    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i ++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(STATUS_SLOT, PresetUtils.loadingItemRed, ChestMenuUtils.getEmptyClickHandler());
        for (int i = 45 ; i < 54 ; i ++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i = 9 ; i < 45 ; i++) {
            blockMenuPreset.addItem(i, PresetUtils.invisibleBackground, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        return new int[0];
    }

    @Override
    public void tick(@Nonnull Block b, @Nonnull Location l, @Nonnull BlockMenu inv) {
        if (!inv.hasViewer()) return;

        List<Location> foundLocations = new ArrayList<>();
        List<String> foundIDs = new ArrayList<>();
        MutableInt length = new MutableInt(0);
        MutableBoolean maxSize = new MutableBoolean(false);
        MutableBoolean maxLength = new MutableBoolean(false);

        inputFlow(length, l, foundLocations, foundIDs, new ArrayList<>(), l);

        int size = foundIDs.size();
        if (maxSize.booleanValue()) {
            size = MAX;
        }
        if (maxLength.booleanValue()) {
            length.setValue(RANGE);
        }
        Material material = Material.ORANGE_STAINED_GLASS_PANE;
        String name = "&6No storage units connected";

        if (size > 0)  {
            material = Material.LIME_STAINED_GLASS_PANE;
            name = "&aConnected to Storage Network";
        }

        inv.replaceExistingItem(STATUS_SLOT, new CustomItem(
                material, name, "&6Units: &e" + size + " / " + MAX, "&6Length: &e" + length + " / " + RANGE
        ));

        for (int i = 9 ; i < 45 ; i++) {
            inv.replaceExistingItem(i, PresetUtils.invisibleBackground);
        }

        int spot = 0;
        for (String id : UNITS) {
            int index = 0;
            for (String found : foundIDs) {
                if (id.equals(found)) {
                    displayStatus(inv, spot, foundLocations.get(index), found);
                    spot++;
                }
                index++;
            }
        }
    }

    /**
     * This method will search for connected storage units
     *
     * @param length length of network so far
     * @param thisLocation location being checked
     * @param foundLocations list of units
     * @param foundIDs list of found unit ids
     * @param checkedLocations checked locations
     * @param prev previous location
     */
    @ParametersAreNonnullByDefault
    private boolean inputFlow(MutableInt length, Location thisLocation, List<Location> foundLocations, List<String> foundIDs, List<Location> checkedLocations, Location prev) {
        checkedLocations.add(thisLocation);

        if (length.intValue() >= RANGE || foundIDs.size() >= MAX) {
            return true;
        }

        Material thisMaterial = thisLocation.getBlock().getType();

        if (thisMaterial == Material.AIR) {
            return false;
        }

        for (int i = 0 ; i < CONNECTABLE_MATS.length ; i++) {
            if (thisMaterial != CONNECTABLE_MATS[i]) {
                continue;
            }

            String thisID = BlockStorage.checkID(thisLocation);

            if (Objects.equals(thisID, CONNECTABLE_IDS[i])) {

                length.increment();

                for (String unit : UNITS) { //add if unit
                    if (thisID.equals(unit)) {
                        foundIDs.add(thisID);
                        foundLocations.add(thisLocation);
                        break;
                    }
                }

                for (Location location : LocationUtils.getAdjacentLocations(thisLocation, false)) {

                    if (location != prev && !checkedLocations.contains(location)) {

                        //try input flow on each unless the max length is reached
                        if (inputFlow(length, location, foundLocations, foundIDs, checkedLocations, location)) {
                            break;
                        }
                    }
                }
            }

            break;
        }

        return false;
    }

    /**
     * This method has does a series of checks before displaying the storage and it's item in a BlockMenu
     *
     * @param inv BlockMenu to display in
     * @param spot spot to display in
     * @param l location of storage unit
     * @param id id of storage unit
     */
    private static void displayStatus(@Nonnull BlockMenu inv, int spot, @Nonnull Location l, @Nonnull String id) {

        ItemStack unit = StackUtils.getItemFromID(id, 1);
        ItemStack item = new CustomItem(Material.GRAY_STAINED_GLASS_PANE, "&8Nothing stored");

        if (unit == null) {
            unit = new CustomItem(Material.BARRIER, "&cERROR");
        }

        String amount = BlockStorage.getLocationInfo(l, "stored");

        if (amount != null) {

            int stored = Integer.parseInt(amount);

            if (stored > 0) {

                String type = BlockStorage.getLocationInfo(l, "storeditem");

                if (type != null) {

                    ItemStack storedItemStack = StackUtils.getItemFromID(type, 1);

                    if (storedItemStack != null) {

                        item = StorageUnit.makeDisplayItem(getMax(id), storedItemStack, stored, id.equals("INFINITY_STORAGE"));
                    }
                }
            }
        }

        displayItem(inv, unit, item, spot);
    }

    /**
     * This method will display 2 the stored item and machine in an inventory at a specified spot
     *
     * @param inv BlockMenu to display in
     * @param item1 machine
     * @param item2 stored item
     * @param spot range 0 - MAX
     */
    private static void displayItem(BlockMenu inv, ItemStack item1, ItemStack item2, int spot) {
        int rows = (int) (1 + Math.floor((float) spot / 9));
        int slot = rows * 9 + spot;
        inv.replaceExistingItem(slot, item1);
        inv.replaceExistingItem(slot + 9, item2);
    }

    /**
     * This method gets the max storage of a storage unit from its id
     *
     * @param id id
     * @return the max storage
     */
    private static int getMax(@Nonnull String id) {
        switch (id) {
            case "BASIC_STORAGE":
                return StorageUnit.BASIC;
            case "ADVANCED_STORAGE":
                return StorageUnit.ADVANCED;
            case "REINFORCED_STORAGE":
                return StorageUnit.REINFORCED;
            case "VOID_STORAGE":
                return StorageUnit.VOID;
            case "INFINITY_STORAGE":
                return StorageUnit.INFINITY;
        }
        return 0;
    }
}