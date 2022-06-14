package org.ximure.spjoin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ConnectionScreen extends Screen {
    private TextFieldWidget ipInputText;

    public ConnectionScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        // enter ip text field
        ipInputText =  this.addDrawableChild(new TextFieldWidget(this.textRenderer, width / 2 - 38, height / 2, 100, 15, null));

        // back button
        this.addDrawableChild(new ButtonWidget(
                this.width - 85,
                this.height - 30,
                70,
                20,
                new LiteralText("Back"), (button) -> {
            client.setScreen(new MultiplayerScreen(null));
        }));

        // connect button
         this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 25,
                this.height / 2 + 20,
                70,
                20,
                new LiteralText("Connect"), (button -> {
                    client.setScreen(new PingConnectionScreen(new LiteralText("Connection Screen"), ipInputText.getText()));
        })));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(new MatrixStack(), textRenderer, new LiteralText("No slots? No problem"), this.width / 2 + 13, this.height / 2 - 15, 14276563);
    }
}
