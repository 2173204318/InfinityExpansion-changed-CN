package io.github.mooy1.infinityexpansion.items.mobdata;

import org.bukkit.Material;

public enum MobDataTier {

    // ex: chicken
    PASSIVE(1, 300, Material.IRON_CHESTPLATE),

    // ex: slime
    NEUTRAL(1, 600, Material.IRON_CHESTPLATE),

    // ex: zombie
    HOSTILE(2, 1200, Material.DIAMOND_CHESTPLATE),

    // ex: endermen
    ADVANCED(4, 2400, Material.DIAMOND_CHESTPLATE),

    // ex: wither
    MINI_BOSS(32, 18000, Material.NETHERITE_CHESTPLATE),

    // ex: ender dragon
    BOSS(96, 36000, Material.NETHERITE_CHESTPLATE);

    final int xp;
    final int energy;
    final Material material;

    MobDataTier(int xp, int energy, Material material) {
        this.xp = (int) (xp * MobSimulationChamber.XP_MULTIPLIER);
        this.energy = energy;
        this.material = material;
    }

}
