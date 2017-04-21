package net.glowstone.scripty.net;

import io.netty.buffer.ByteBuf;
import net.glowstone.scripty.ScriptLanguage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScriptyPacketContent implements IMessage {

    private String content;
    private ScriptLanguage language;

    public ScriptyPacketContent() {
    }

    public ScriptyPacketContent(String content, ScriptLanguage language) {
        this.content = content;
        this.language = language;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        int contentLength = buffer.readVarInt();
        String content = buffer.readString(contentLength);
        int languageId = buffer.readByte();
        ScriptLanguage language = ScriptLanguage.values()[languageId];
        setContent(content);
        setLanguage(language);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeVarInt(content.length());
        buffer.writeString(content);
        buffer.writeByte(language.ordinal());
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

    public void setLanguage(ScriptLanguage language) {
        this.language = language;
    }

    public static class Handler implements IMessageHandler<ScriptyPacketContent, IMessage> {
        @Override
        public IMessage onMessage(ScriptyPacketContent message, MessageContext ctx) {
            ScriptyNetworkHandler.handleContentMessage(message);
            return message;
        }
    }
}
