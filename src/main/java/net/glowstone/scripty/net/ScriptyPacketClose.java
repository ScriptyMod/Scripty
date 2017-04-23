package net.glowstone.scripty.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScriptyPacketClose implements IMessage {
    private BlockPos pos;

    public ScriptyPacketClose() {
    }

    public ScriptyPacketClose(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        pos = buffer.readBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeBlockPos(pos);
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public static class HandlerServer implements IMessageHandler<ScriptyPacketClose, IMessage> {
        @Override
        public IMessage onMessage(ScriptyPacketClose message, MessageContext ctx) {
            ScriptyNetworkHandler.handleCloseMessage(message, ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}
