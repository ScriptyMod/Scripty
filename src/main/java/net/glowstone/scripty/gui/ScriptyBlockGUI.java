package net.glowstone.scripty.gui;

import net.glowstone.scripty.ScriptLanguage;
import net.glowstone.scripty.net.ScriptyNetworkHandler;
import net.glowstone.scripty.net.ScriptyPacketContent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class ScriptyBlockGUI extends GuiScreen {

    private BlockPos pos;
    private String content;
    private ScriptLanguage language;
    private GuiButton languageToggle;

    public ScriptyBlockGUI() {
        this.content = "";
        this.language = ScriptLanguage.PYTHON;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ScriptLanguage getLanguage() {
        return language;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setLanguage(ScriptLanguage language) {
        this.language = language;
        if (this.languageToggle != null) {
            this.languageToggle.displayString = language.getName();
        }
    }

    public ScriptyBlockGUI(BlockPos pos, String content, ScriptLanguage language) {
        this.pos = pos;
        this.content = content;
        this.language = language;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.languageToggle = this.addButton(new GuiButton(0x0, this.width - 50 - 100, 20, 100, 20, language.getName()));
    }

    @Override
    public void onGuiClosed() {
        // send update packet
        ScriptyNetworkHandler.sendContentUpdate(new ScriptyPacketContent(pos, content, language));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == languageToggle.id) {
            int ordinal = language.ordinal() + 1;
            if (ordinal >= ScriptLanguage.values().length) {
                ordinal = 0;
            }
            setLanguage(ScriptLanguage.values()[ordinal]);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.drawString(this.fontRendererObj, "Script Block", 50, 30 - 4, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
