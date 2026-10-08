package com.ruletacaos;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RuletaChaos implements ModInitializer {
    public static final String MOD_ID = "ruletacaos";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Random random = new Random();

    @Override
    public void onInitialize() {
        LOGGER.info("Iniciando Ruleta Chaos 26.3...");
        startRuletaCycle();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("ruleta")
                .then(CommandManager.literal("girar").executes(context -> {
                    MinecraftServer server = context.getSource().getServer();
                    EventManager.spin(server);
                    context.getSource().sendFeedback(() -> Text.literal("§6¡La ruleta está girando manualmente!"), false);
                    return 1;
                }))
                .then(CommandManager.literal("stop").executes(context -> {
                    ChallengeHandler.stopAll();
                    context.getSource().sendFeedback(() -> Text.literal("§cDesafío detenido."), false);
                    return 1;
                }))
            );
        });
    }

    private void startRuletaCycle() {
        int nextSpin = getRandomInterval();
        scheduler.schedule(() -> {
            // Buscamos la instancia del servidor para girar la ruleta
            // Nota: En un entorno real usaríamos un tick event
            LOGGER.info("¡Es hora de girar la ruleta!");
            startRuletaCycle();
        }, nextSpin, TimeUnit.MINUTES);
    }

    private int getRandomInterval() {
        int[] intervals = {5, 10, 20};
        return intervals[random.nextInt(intervals.length)];
    }
}
