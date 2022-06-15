package org.ximure.spjoin.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.ximure.spjoin.Utils;

import java.util.List;

public class PingConnectionScreen extends Screen {
    private final String serverAddress;

    public PingConnectionScreen(Text title, String serverAddress) {
        super(title);
        this.serverAddress = serverAddress;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(
                this.width - 85,
                this.height - 30,
                70,
                20,
                new LiteralText("Back"), (button) -> {
            client.setScreen(new ConnectionScreen(new LiteralText("Connection Screen")));
        }));

        startCountdown();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(new MatrixStack(), textRenderer, new LiteralText("Waiting for a free slot at " + serverAddress + "..."), this.width / 2 + 13, this.height / 2 - 15, 14276563);
    }

    private void startCountdown() {
        new Thread(() -> {
            while (true) {
                List<Integer> pingResult = Utils.pingServer(serverAddress);

                if (pingResult == null) {
                    MinecraftClient.getInstance().execute(this::setErrorScreen);
                    break;
                }

                Integer maxPlayers = pingResult.get(1);
                Integer currentPlayers = pingResult.get(0);

                if (currentPlayers < maxPlayers) {
                    MinecraftClient.getInstance().execute(this::connect);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (!MinecraftClient.getInstance().currentScreen.getClass().equals(DisconnectedScreen.class)) {
                        break;
                    }

                    if (MinecraftClient.getInstance().currentScreen.getClass().equals(DisconnectedScreen.class)) {
                        MinecraftClient.getInstance().execute(this::setScreen);
                    }

                    if (!MinecraftClient.getInstance().currentScreen.getClass().equals(PingConnectionScreen.class)) {
                        break;
                    }
                }

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void setErrorScreen() {
        MinecraftClient.getInstance().setScreen(new ErrorScreen(new LiteralText("Error screen")));
    }

    private void setScreen() {
        MinecraftClient.getInstance().setScreen(this);
    }

    private void connect() {
        ConnectScreen.connect(
                new MultiplayerScreen(new TitleScreen()),
                MinecraftClient.getInstance(),
                ServerAddress.parse(serverAddress),
                null);
    }
}
