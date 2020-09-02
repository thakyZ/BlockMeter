package win.baruna.blockmeter.measurebox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import win.baruna.blockmeter.BlockMeterClient;
import win.baruna.blockmeter.ModConfig;

public class ClientMeasureBox extends MeasureBox {
    private Box box;

    protected ClientMeasureBox(final BlockPos blockStart,
            final BlockPos blockEnd,
            final Identifier dimension, final DyeColor color,
            final boolean finished, final int mode,
            final int orientation) {
        super(blockStart, blockEnd, dimension, color, finished, mode,
                orientation);
        updateBoundingBox();
    }

    protected ClientMeasureBox(PacketByteBuf attachedData) {
        super(attachedData);
        updateBoundingBox();
    }

    public static ClientMeasureBox getBox(final BlockPos block,
            final Identifier dimension) {
        final ClientMeasureBox box = new ClientMeasureBox(block, block,
                dimension, getSelectedColor(), false, 0, 0);
        incrementColor();
        return box;
    }

    /**
     * Sets the second box corner
     * 
     * @param block
     *            second corner position
     */
    public void setBlockEnd(final BlockPos block) {
        blockEnd = block;
        updateBoundingBox();
    }

    /**
     * The current creation state of the MeasureBox
     * 
     * @return true if MeasureBox is completed
     */
    public boolean isFinished() {
        return this.finished;
    }

    /**
     * Markes Box to be complete
     */
    public void setFinished() {
        this.finished = true;
    }

    /**
     * Sets the Color of the MeasureBox
     * 
     * @param color
     *            Color to be applied
     */
    public void setColor(final DyeColor color) {
        this.color = color;
    }

    /**
     * Renders the Box
     * 
     * @param camera
     *            rendering Camera
     * @param stack
     * @param currentDimension
     *            Dimension the Player currently is in
     */
    public void render(final Camera camera, final MatrixStack stack,
            final Identifier currentDimension) {
        render(camera, stack, currentDimension, null);
    }

    /**
     * Renders the Box
     * 
     * @param camera
     *            rendering Camera
     * @param stack
     * @param currentDimension
     *            Dimension the Player currently is in
     * @param boxCreatorName
     *            Name of the Player that created the Box
     */
    public void render(final Camera camera, final MatrixStack stack,
            final Identifier currentDimension,
            final Text boxCreatorName) {
        if (!(currentDimension.equals(this.dimension))) {
            return;
        }
        final Vec3d pos = camera.getPos();

        RenderSystem.lineWidth(2.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();

        stack.push();
        stack.translate(-pos.x, -pos.y, -pos.z);
        final Matrix4f model = stack.peek().getModel();

        final Tessellator tess = Tessellator.getInstance();
        final BufferBuilder buffer = tess.getBuffer();
        final float[] color = this.color.getColorComponents();
        final float r = color[0];
        final float g = color[1];
        final float b = color[2];
        final float a = 0.8f;

        buffer.begin(3, VertexFormats.POSITION_COLOR);

        buffer.vertex(model, (float) this.box.minX, (float) this.box.minY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.minY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.minY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.minX, (float) this.box.minY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.minX, (float) this.box.minY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();

        buffer.vertex(model, (float) this.box.minX, (float) this.box.maxY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();

        buffer.vertex(model, (float) this.box.minX, (float) this.box.maxY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.maxY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.maxY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.minX, (float) this.box.maxY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.minX, (float) this.box.maxY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();

        buffer.vertex(model, (float) this.box.minX, (float) this.box.maxY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.minX, (float) this.box.minY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();

        buffer.vertex(model, (float) this.box.maxX, (float) this.box.minY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.maxY,
                (float) this.box.maxZ).color(r, g, b, a)
                .next();

        buffer.vertex(model, (float) this.box.maxX, (float) this.box.maxY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        buffer.vertex(model, (float) this.box.maxX, (float) this.box.minY,
                (float) this.box.minZ).color(r, g, b, a)
                .next();
        tess.draw();

        if (BlockMeterClient.getConfigManager().getConfig().innerDiagonal) {
            buffer.begin(1, VertexFormats.POSITION_COLOR);
            buffer.vertex(model, (float) this.box.minX, (float) this.box.minY,
                    (float) this.box.minZ).color(r, g, b, a)
                    .next();
            buffer.vertex(model, (float) this.box.maxX, (float) this.box.maxY,
                    (float) this.box.maxZ).color(r, g, b, a)
                    .next();
            tess.draw();

        }
        RenderSystem.enableTexture();
        RenderSystem.lineWidth(1.0f);

        this.drawLengths(camera, stack, boxCreatorName);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        stack.pop();
    }

    /**
     * Tests if the block is on a corner of the box
     * 
     * @param block
     *            Position to test
     * @return true if block is a corner
     */
    public boolean isCorner(final BlockPos block) {
        return (block.getX() == blockStart.getX()
                || block.getX() == blockEnd.getX())
                && (block.getY() == blockStart.getY()
                        || block.getY() == blockEnd.getY())
                && (block.getZ() == blockStart.getZ()
                        || block.getZ() == blockEnd.getZ());
    }

    /**
     * Loosens the selected Corner i.e. the opposite corner gets fixed, and the
     * current one can be moved
     * 
     * @param block
     *            The corner to loosen, needs to be an actual corner of the box
     */
    public void loosenCorner(final BlockPos block) {
        final int x = blockStart.getX() == block.getX() ? blockEnd.getX()
                : blockStart.getX();
        final int y = blockStart.getY() == block.getY() ? blockEnd.getY()
                : blockStart.getY();
        final int z = blockStart.getZ() == block.getZ() ? blockEnd.getZ()
                : blockStart.getZ();
        blockStart = new BlockPos(x, y, z);
        blockEnd = block;
        finished = false;
    }

    /**
     * Calculates the BoundingBox for rendering
     */
    private void updateBoundingBox() {
        final int ax = this.blockStart.getX();
        final int ay = this.blockStart.getY();
        final int az = this.blockStart.getZ();
        final int bx = this.blockEnd.getX();
        final int by = this.blockEnd.getY();
        final int bz = this.blockEnd.getZ();

        this.box = new Box((double) Math.min(ax, bx), (double) Math.min(ay, by),
                (double) Math.min(az, bz),
                (double) (Math.max(ax, bx) + 1),
                (double) (Math.max(ay, by) + 1),
                (double) (Math.max(az, bz) + 1));

    }

    /**
     * Draws the length label
     * 
     * @param camera
     *            rendering Camera
     * @param stack
     * @param boxCreatorName
     *            Name of the Box creator
     */
    private void drawLengths(final Camera camera, final MatrixStack stack,
            final Text boxCreatorName) {
        final int lengthX = (int) this.box.getXLength();
        final int lengthY = (int) this.box.getYLength();
        final int lengthZ = (int) this.box.getZLength();

        final Vec3d boxCenter = this.box.getCenter();
        final double diagonalLength = new Vec3d(this.box.minX, this.box.minY,
                this.box.minZ)
                        .distanceTo(new Vec3d(this.box.maxX, this.box.maxY,
                                this.box.maxZ));

        final float yaw = camera.getYaw();
        final float pitch = camera.getPitch();
        final Vec3d pos = camera.getPos();

        final List<Line> lines = new ArrayList<>();
        lines.add(new Line(
                new Box(this.box.minX, this.box.minY, this.box.minZ,
                        this.box.minX, this.box.minY, this.box.maxZ),
                pos));
        lines.add(new Line(
                new Box(this.box.minX, this.box.maxY, this.box.minZ,
                        this.box.minX, this.box.maxY, this.box.maxZ),
                pos));
        lines.add(new Line(
                new Box(this.box.maxX, this.box.minY, this.box.minZ,
                        this.box.maxX, this.box.minY, this.box.maxZ),
                pos));
        lines.add(new Line(
                new Box(this.box.maxX, this.box.maxY, this.box.minZ,
                        this.box.maxX, this.box.maxY, this.box.maxZ),
                pos));
        Collections.sort(lines);
        final Vec3d lineZ = lines.get(0).line.getCenter();

        lines.clear();
        lines.add(new Line(
                new Box(this.box.minX, this.box.minY, this.box.minZ,
                        this.box.minX, this.box.maxY, this.box.minZ),
                pos));
        lines.add(new Line(
                new Box(this.box.minX, this.box.minY, this.box.maxZ,
                        this.box.minX, this.box.maxY, this.box.maxZ),
                pos));
        lines.add(new Line(
                new Box(this.box.maxX, this.box.minY, this.box.minZ,
                        this.box.maxX, this.box.maxY, this.box.minZ),
                pos));
        lines.add(new Line(
                new Box(this.box.maxX, this.box.minY, this.box.maxZ,
                        this.box.maxX, this.box.maxY, this.box.maxZ),
                pos));
        Collections.sort(lines);
        final Vec3d lineY = lines.get(0).line.getCenter();

        lines.clear();
        lines.add(new Line(
                new Box(this.box.minX, this.box.minY, this.box.minZ,
                        this.box.maxX, this.box.minY, this.box.minZ),
                pos));
        lines.add(new Line(
                new Box(this.box.minX, this.box.minY, this.box.maxZ,
                        this.box.maxX, this.box.minY, this.box.maxZ),
                pos));
        lines.add(new Line(
                new Box(this.box.minX, this.box.maxY, this.box.minZ,
                        this.box.maxX, this.box.maxY, this.box.minZ),
                pos));
        lines.add(new Line(
                new Box(this.box.minX, this.box.maxY, this.box.maxZ,
                        this.box.maxX, this.box.maxY, this.box.maxZ),
                pos));
        Collections.sort(lines);
        final Vec3d lineX = lines.get(0).line.getCenter();

        final String playerNameStr = (boxCreatorName == null ? ""
                : boxCreatorName.getString() + " : ");
        if (BlockMeterClient.getConfigManager().getConfig().innerDiagonal) {
            this.drawText(stack, boxCenter.x, boxCenter.y, boxCenter.z, yaw,
                    pitch,
                    playerNameStr + String.format("%.2f", diagonalLength), pos);
        }
        this.drawText(stack, lineX.x, lineX.y, lineX.z, yaw, pitch,
                playerNameStr + String.valueOf(lengthX), pos);
        this.drawText(stack, lineY.x, lineY.y, lineY.z, yaw, pitch,
                playerNameStr + String.valueOf(lengthY), pos);
        this.drawText(stack, lineZ.x, lineZ.y, lineZ.z, yaw, pitch,
                playerNameStr + String.valueOf(lengthZ), pos);
    }

    /**
     * Draws a text with orientation and position
     * 
     * @param stack
     * @param x
     * @param y
     * @param z
     * @param yaw
     * @param pitch
     * @param text
     * @param playerPos
     */
    private void drawText(final MatrixStack stack, final double x,
            final double y, final double z, final float yaw,
            final float pitch, final String text, final Vec3d playerPos) {
        @SuppressWarnings("resource")
        final TextRenderer textRenderer = MinecraftClient
                .getInstance().textRenderer;

        final LiteralText literalText = new LiteralText(text);

        float size = 0.03f;
        final int constDist = 10;

        if (AutoConfig.getConfigHolder(ModConfig.class)
                .getConfig().minimalLabelSize) {
            final float dist = (float) Math
                    .sqrt((x - playerPos.x) * (x - playerPos.x)
                            + (y - playerPos.y) * (y - playerPos.y)
                            + (z - playerPos.z) * (z - playerPos.z));
            if (dist > constDist)
                size = dist * size / constDist;
        }

        stack.push();
        stack.translate(x, y + 0.15, z);
        stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw));
        stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-pitch));
        stack.scale(size, -size, 0.001f);
        final int width = textRenderer.getWidth(literalText);
        stack.translate((-width / 2), 0.0, 0.0);
        final Matrix4f model = stack.peek().getModel();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        final float[] colors = this.color.getColorComponents();
        buffer.begin(7, VertexFormats.POSITION_COLOR);
        buffer.vertex(model, -1, -1, 0)
                .color(colors[0], colors[1], colors[2], 0.8f).next();
        buffer.vertex(model, -1, 8, 0)
                .color(colors[0], colors[1], colors[2], 0.8f).next();
        buffer.vertex(model, width, 8, 0)
                .color(colors[0], colors[1], colors[2], 0.8f).next();
        buffer.vertex(model, width, -1, 0)
                .color(colors[0], colors[1], colors[2], 0.8f).next();
        Tessellator.getInstance().draw();

        final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider
                .immediate(buffer);
        textRenderer.draw(literalText, 0.0f, 0.0f, this.color.getSignColor(),
                false, // shadow
                model, // matrix
                immediate, // draw buffer
                true, // seeThrough
                0, // backgroundColor => underlineColor,
                0 // light
        );
        immediate.draw();

        stack.pop();
    }

    /**
     * If enabled increments to next color
     */
    static private void incrementColor() {
        final ModConfig conf = BlockMeterClient.getConfigManager().getConfig();

        if (conf.incrementColor) {
            setColorIndex(conf.colorIndex + 1);
        }
    }

    /**
     * Accessor for the currently selected color
     * 
     * @return currently selected color
     */
    static private DyeColor getSelectedColor() {
        final ModConfig conf = BlockMeterClient.getConfigManager().getConfig();
        return DyeColor.byId(conf.colorIndex);
    }

    public static void setColorIndex(final int newColor) {
        BlockMeterClient.getConfigManager().getConfig().colorIndex = Math
                .floorMod(newColor, DyeColor.values().length);
        BlockMeterClient.getConfigManager().save();
    }

    /**
     * Parses a ClientMeasureBox from a PacketByteBuf
     * 
     * @param attachedData
     *            a PacketByteBuf containing the ClientMeasureBox
     * @return the PacketByteBuf submitted
     */
    public static ClientMeasureBox fromPacketByteBuf(
            final PacketByteBuf attachedData) {
        return new ClientMeasureBox(attachedData);
    }

    private class Line implements Comparable<Line> {
        Box line;
        double distance;

        Line(final Box line, final Vec3d pos) {
            this.line = line;
            this.distance = line.getCenter().distanceTo(pos);
        }

        @Override
        public int compareTo(final Line l) {
            return Double.compare(this.distance, l.distance);
        }
    }
}
