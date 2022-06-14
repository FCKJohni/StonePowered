package dev.teamhelios.stonepowered.loader;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.loader.utils.ILoader;
import dev.teamhelios.stonepowered.loader.utils.LoaderStatus;
import dev.teamhelios.stonepowered.loader.utils.Runnable;
import io.javalin.Javalin;

public class WebsocketLoader extends ILoader {

    private Runnable<LoaderStatus> callback;
    private StonePowered stonePowered;
    private Javalin javalin;

    @Override
    public void setup(Runnable<LoaderStatus> callback, StonePowered stonePowered) {
        this.callback = callback;
        this.stonePowered = stonePowered;
    }

    @Override
    public void initLoader() {
        notifyUpdate(LoaderStatus.LOADED, callback);
        javalin = Javalin.create();
        javalin._conf.showJavalinBanner = false;
        javalin.start(9999);
        javalin.sse("/setupClient", client -> {
            client.sendEvent("Connected", "Hello from StonePowered!");
            stonePowered.getSoil().getClientHandler().register(client);
            client.onClose(() -> stonePowered.getSoil().getClientHandler().unregister(client));
        });
    }

    @Override
    public void shutdown() {
        notifyUpdate(LoaderStatus.UNLOADED, callback);
    }

    public Javalin getJavalin() {
        return javalin;
    }
}
