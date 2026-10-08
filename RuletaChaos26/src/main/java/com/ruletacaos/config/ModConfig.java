package com.ruletacaos.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruletacaos.RuletaChaos;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ruletacaos.json");

    public List<Integer> intervalMinutes = Arrays.asList(5, 10, 20);
    public boolean autoSpinEnabled = true;

    public boolean redEnabled = true;
    public boolean yellowEnabled = true;
    public boolean blueEnabled = true;
    public boolean greenEnabled = true;
    public boolean orangeEnabled = true;
    public boolean pinkEnabled = true;
    public boolean violetEnabled = true;
    public boolean cyanEnabled = true;

    public int redMinDurationSeconds = 180;
    public int redMaxDurationSeconds = 300;
    public List<String> redChallenges = Arrays.asList(
            "height_250", "y_minus_50", "life_drain", "creeper_spawn",
            "zombie_skeleton_horde", "meteor_rain", "no_standing_still",
            "extreme_hunger", "lightning_storm"
    );

    public int yellowDifficulty = 1;
    public boolean yellowCreepersFaster = true;
    public boolean yellowZombiesStronger = true;
    public boolean yellowMobsMoreHealth = true;
    public boolean yellowMoreMobs = true;
    public boolean yellowNoBurnDay = true;
    public boolean yellowSlowRegen = true;
    public boolean yellowDoubleXp = true;

    public boolean blueSpawnFromSky = true;
    public boolean blueGuarded = true;
    public boolean blueTrapChance = true;
    public double blueTrapChancePercent = 15.0;

    public boolean greenBigPrizeEnabled = true;

    public List<String> orangeEffects = Arrays.asList(
            "moon_gravity", "animal_rain", "gather_all", "shuffle_inventory",
            "crazy_drops", "double_speed", "crazy_time"
    );

    public List<String> pinkAllies = Arrays.asList(
            "tamed_wolf", "horse_with_saddle", "iron_golem", "tamed_cat", "allay"
    );

    public List<String> violetEffects = Arrays.asList(
            "levitation", "random_teleport", "mystery_potions", "glowing"
    );

    public List<String> cyanEffects = Arrays.asList(
            "storm_or_clear", "item_rain", "double_mining"
    );

    public boolean showNotification = true;
    public int notificationDurationSeconds = 8;
    public String hideKey = "I";

    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                if (config != null) {
                    RuletaChaos.LOGGER.info("Configuración cargada desde {}", CONFIG_FILE.getAbsolutePath());
                    return config;
                }
            } catch (Exception e) {
                RuletaChaos.LOGGER.error("Error cargando config, se usará la por defecto", e);
            }
        }
        ModConfig defaultConfig = new ModConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
            RuletaChaos.LOGGER.info("Configuración guardada en {}", CONFIG_FILE.getAbsolutePath());
        } catch (IOException e) {
            RuletaChaos.LOGGER.error("No se pudo guardar la configuración", e);
        }
    }
}
