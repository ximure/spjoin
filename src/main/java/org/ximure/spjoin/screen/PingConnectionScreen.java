package org.ximure.spjoin.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
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

        startPinging();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void startPinging() {
        new Thread(() -> {
            while (true) {
                List<Integer> pingResult = Utils.pingServer(serverAddress);

                if (pingResult == null) {
                    MinecraftClient.getInstance().setScreen(new ConnectionScreen(new LiteralText("ping result == null")));
                    break;
                }

                Integer maxPlayers = pingResult.get(1);
                Integer currentPlayers = pingResult.get(0);

                if (currentPlayers < maxPlayers) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void connectToServer() {
        ConnectScreen.connect(
                this,
                MinecraftClient.getInstance(),
                ServerAddress.parse(serverAddress),
                null
        );

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
