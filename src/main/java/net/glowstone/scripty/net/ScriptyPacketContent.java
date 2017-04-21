package net.glowstone.scripty.net;

import io.netty.buffer.ByteBuf;
import net.glowstone.scripty.ScriptLanguage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScriptyPacketContent implements IMessage {

    private BlockPos pos;
    private String content;
    private ScriptLanguage language;

    public ScriptyPacketContent() {
    }

    public ScriptyPacketContent(BlockPos pos, String content, ScriptLanguage language) {
        this.pos = pos;
        this.content = content;
        this.language = language;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        int x = buffer.readVarInt();
        int y = buffer.readVarInt();
        int z = buffer.readVarInt();
        BlockPos pos = new BlockPos(x, y, z);
        int contentLength = buffer.readVarInt();
        String content = buffer.readString(contentLength);
        int languageId = buffer.readByte();
        ScriptLanguage language = ScriptLanguage.values()[languageId];
        setContent(content);
        setLanguage(language);
        setPos(pos);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeVarInt(pos.getX());
        buffer.writeVarInt(pos.getY());
        buffer.writeVarInt(pos.getZ());
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

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public static class HandlerClient implements IMessageHandler<ScriptyPacketContent, IMessage> {
        @Override
        public IMessage onMessage(ScriptyPacketContent message, MessageContext ctx) {
            ScriptyNetworkHandler.handleContentMessage(message);
            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<ScriptyPacketContent, IMessage> {
        @Override
        public IMessage onMessage(ScriptyPacketContent message, MessageContext ctx) {
            ScriptyNetworkHandler.handleContentUpdate(message, ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}
