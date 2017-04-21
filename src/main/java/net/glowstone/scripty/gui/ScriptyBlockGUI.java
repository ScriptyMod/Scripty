package net.glowstone.scripty.gui;

import net.glowstone.scripty.ScriptLanguage;
import net.glowstone.scripty.net.ScriptyNetworkHandler;
import net.glowstone.scripty.net.ScriptyPacketContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

public class ScriptyBlockGUI extends GuiScreen {

    private static final String SAVE_CHANGES = "Save changes";
    private static final String SAVE_CHANGES_LIVE = new TextComponentString(SAVE_CHANGES).setStyle(new Style().setColor(TextFormatting.YELLOW)).getFormattedText();

    private BlockPos pos;
    private String content;
    private ScriptLanguage language;

    // buttons
    private boolean changes = false;
    private GuiButton languageToggle;
    private GuiButton saveButton;

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
        this.buttonList.clear();
        this.languageToggle = this.addButton(new GuiButton(0x0, this.width - 50 - 100, 20, 100, 20, language.getName()));
        this.saveButton = this.addButton(new GuiButton(0x1, this.width - 50 - 100, this.height - 40, 100, 20, SAVE_CHANGES));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == languageToggle.id) {
            int ordinal = language.ordinal() + 1;
            if (ordinal >= ScriptLanguage.values().length) {
                ordinal = 0;
            }
            setLanguage(ScriptLanguage.values()[ordinal]);
            changes = true;
        } else if (button.id == saveButton.id) {
            if (changes) {
                ScriptyNetworkHandler.sendContentUpdate(new ScriptyPacketContent(pos, content, language));
            }
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }
        if (changes) {
            saveButton.displayString = SAVE_CHANGES_LIVE;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.drawString(this.fontRendererObj, "Script Block", 50, 30 - 4, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
