package com.ruletacaos.effect;

import net.minecraft.server.MinecraftServer;

public abstract class ActiveChallenge {
    protected final String name;
    protected final int durationTicks;
    protected int ticksLeft;
    protected boolean finished = false;

    public ActiveChallenge(String name, int durationSeconds) {
        this.name = name;
        this.durationTicks = durationSeconds * 20;
        this.ticksLeft = this.durationTicks;
    }

    public String getName() {
        return name;
    }

    public float getProgress() {
        return (float) ticksLeft / (float) durationTicks;
    }

    public boolean isFinished() {
        return finished || ticksLeft <= 0;
    }

    public void tick(MinecraftServer server) {
        if (finished) return;
        ticksLeft--;
        onTick(server);
        if (ticksLeft <= 0) {
            finished = true;
        }
    }

    public abstract void start(MinecraftServer server);
    public abstract void onTick(MinecraftServer server);
    public abstract void end(MinecraftServer server);
}
