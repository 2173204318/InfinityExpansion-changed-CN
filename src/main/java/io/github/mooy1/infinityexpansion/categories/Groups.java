package io.github.mooy1.infinityexpansion.categories;

import org.bukkit.Material;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinitylib.groups.MultiGroup;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * Categories for this addon
 *
 * @author Mooy1
 */
public final class Groups {

        public static final ItemGroup INFINITY = new InfinityGroup(InfinityExpansion.createKey("infinity_recipes"),
        new CustomItemStack(Material.RESPAWN_ANCHOR, "&e天空无尽 &7- &6终极物品"), 3);
public static final ItemGroup MAIN_MATERIALS = new SubGroup("main_materials",
        new CustomItemStack(Material.NETHER_STAR, "&e天空无尽 &7- &6基础材料"));
public static final ItemGroup BASIC_MACHINES = new SubGroup("basic_machines",
        new CustomItemStack(Material.LOOM, "&e天空无尽 &7- &6基础机器"));
public static final ItemGroup ADVANCED_MACHINES = new SubGroup("advanced_machines",
        new CustomItemStack(Material.BLAST_FURNACE, "&e天空无尽 &7- &6高级机器"));
public static final ItemGroup STORAGE = new SubGroup("storage",
        new CustomItemStack(Material.BARRIER, "&e天空无尽 &7- &6此模块已禁用"));
public static final ItemGroup MOB_SIMULATION = new SubGroup("mob_simulation",
        new CustomItemStack(Material.BEACON, "&e天空无尽 &7- &6生物模拟"));
public static final ItemGroup INFINITY_MATERIALS = new SubGroup("infinity_materials",
        new CustomItemStack(Material.NETHERITE_BLOCK, "&e天空无尽 &7- &6材料"));
public static final ItemGroup MAIN_CATEGORY = new MultiGroup("main",
        new CustomItemStack(Material.NETHER_STAR, "&e天空无尽科技"), 3,
        MAIN_MATERIALS, BASIC_MACHINES, ADVANCED_MACHINES, STORAGE, MOB_SIMULATION, INFINITY_MATERIALS, INFINITY);
public static final ItemGroup INFINITY_CHEAT = new SubGroup("infinity_cheat",
        new CustomItemStack(Material.RESPAWN_ANCHOR, "&b无尽 &7物品 &c- 作弊模式"));

    public static void setup(InfinityExpansion inst) {
        INFINITY.register(inst);
        MAIN_CATEGORY.register(inst);
        INFINITY_CHEAT.register(inst);
    }

}
