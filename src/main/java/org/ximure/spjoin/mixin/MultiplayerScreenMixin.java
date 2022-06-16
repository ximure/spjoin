package org.ximure.spjoin.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.ximure.spjoin.screen.PingConnectionScreen;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Shadow private ServerInfo selectedEntry;
    @Shadow protected MultiplayerServerListWidget serverListWidget;
    private Screen connectingScreen;
    private ButtonWidget playOnFullServerButton;

    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void addButton(CallbackInfo callbackInfo) {
        playOnFullServerButton = this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 55,
                7,
                100,
                20,
                new LiteralText("Join full server"), (buttonWidget) -> {
                    this.close();
                    MinecraftClient.getInstance().setScreen(connectingScreen);
        }));

        updateStates();
    }

    @Inject(at = @At("TAIL"), method = "select")
    public void select(MultiplayerServerListWidget.Entry entry, CallbackInfo ci) {
        this.serverListWidget.setSelected(entry);
        this.updateStates();
    }

    private void updateStates() {
        this.playOnFullServerButton.active = false;
        MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
        if (entry != null) {
            if (this.selectedEntry == null) {
                selectedEntry = new ServerInfo("1", "1", true);
            }
            ServerInfo serverInfo = ((MultiplayerServerListWidget.ServerEntry)entry).getServer();
            this.playOnFullServerButton.active = true;
            selectedEntry.address = serverInfo.address;
            selectedEntry.name = serverInfo.name;
            selectedEntry.copyFrom(serverInfo);

            connectingScreen = new PingConnectionScreen(
                    new LiteralText("Join full server"),
                    selectedEntry
            );
        }
    }
}
