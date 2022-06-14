package org.ximure.spjoin.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.ximure.spjoin.screen.ConnectionScreen;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void addButton(CallbackInfo callbackInfo) {
        Screen connectingScreen = new ConnectionScreen(
                new LiteralText("No slots?")
        );

        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 100 + 205,
                5,
                55,
                20,
                new LiteralText("No slots?"), (buttonWidget) -> {
                    this.close();
                    MinecraftClient.getInstance().setScreen(connectingScreen);
        }));
    }
}
