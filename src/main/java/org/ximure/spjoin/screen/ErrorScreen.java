package org.ximure.spjoin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ErrorScreen extends Screen {
    public ErrorScreen(Text title) {
        super(title);
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
            client.setScreen(new ConnectionScreen(new LiteralText("Error screen")));
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(new MatrixStack());
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(new MatrixStack(), textRenderer, new LiteralText("Error pinging this server"), this.width / 2 + 5, this.height / 2 - 15, 14276563);
    }
}
