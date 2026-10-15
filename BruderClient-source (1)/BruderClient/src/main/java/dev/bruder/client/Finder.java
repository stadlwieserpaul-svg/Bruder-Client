package dev.bruder.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Finder {
    public record BlockHit(BlockPos pos, double dist) {}
    public record ChunkHit(ChunkPos chunk, int count) {}

    public static volatile List<BlockHit> blockHits = List.of();
    public static volatile List<ChunkHit> chunkHits = List.of();
    private static int ticks;

    private static Set<Block> targets() {
        Set<Block> set = new HashSet<>();
        for (String s : Config.data.blocks) {
            Identifier id = Identifier.tryParse(s);
            if (id == null) continue;
            Block b = Registries.BLOCK.get(id);
            if (b != Blocks.AIR) set.add(b);
        }
        return set;
    }

    public static void tick(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) return;
        ticks++;
        if (Module.BLOCK_FINDER.enabled && ticks % 40 == 0) scanBlocks(mc);
        if (Module.CHUNK_FINDER.enabled && ticks % 100 == 0) scanChunks(mc);
        if (!Module.BLOCK_FINDER.enabled) blockHits = List.of();
        if (!Module.CHUNK_FINDER.enabled) chunkHits = List.of();
    }

    private static void scanBlocks(MinecraftClient mc) {
        Set<Block> t = targets();
        int r = Math.min(Config.data.finderRadius, 48);
        BlockPos o = mc.player.getBlockPos();
        BlockPos.Mutable m = new BlockPos.Mutable();
        List<BlockHit> out = new ArrayList<>();
        int minY = mc.world.getBottomY(), maxY = mc.world.getTopYInclusive();
        for (int x = -r; x <= r; x++)
            for (int z = -r; z <= r; z++)
                for (int y = Math.max(minY, o.getY() - r); y <= Math.min(maxY, o.getY() + r); y++) {
                    m.set(o.getX() + x, y, o.getZ() + z);
                    if (t.contains(mc.world.getBlockState(m).getBlock()))
                        out.add(new BlockHit(m.toImmutable(), Math.sqrt(m.getSquaredDistance(o))));
                }
        out.sort(Comparator.comparingDouble(BlockHit::dist));
        blockHits = out.size() > 8 ? new ArrayList<>(out.subList(0, 8)) : out;
    }

    private static void scanChunks(MinecraftClient mc) {
        Set<Block> t = targets();
        int r = Math.min(mc.options.getViewDistance().getValue(), 8);
        ChunkPos c0 = mc.player.getChunkPos();
        List<ChunkHit> out = new ArrayList<>();
        for (int dx = -r; dx <= r; dx++)
            for (int dz = -r; dz <= r; dz++) {
                WorldChunk ch = mc.world.getChunkManager().getWorldChunk(c0.x + dx, c0.z + dz);
                if (ch == null) continue;
                int count = 0;
                for (ChunkSection s : ch.getSectionArray()) {
                    if (s.isEmpty() || !s.hasAny(st -> t.contains(st.getBlock()))) continue;
                    for (int x = 0; x < 16; x++)
                        for (int y = 0; y < 16; y++)
                            for (int z = 0; z < 16; z++) {
                                BlockState st = s.getBlockState(x, y, z);
                                if (t.contains(st.getBlock())) count++;
                            }
                }
                if (count > 0) out.add(new ChunkHit(new ChunkPos(c0.x + dx, c0.z + dz), count));
            }
        out.sort(Comparator.comparingInt(ChunkHit::count).reversed());
        chunkHits = out.size() > 5 ? new ArrayList<>(out.subList(0, 5)) : out;
    }
}
