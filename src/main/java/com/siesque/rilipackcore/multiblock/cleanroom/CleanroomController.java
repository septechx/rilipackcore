package com.siesque.rilipackcore.multiblock.cleanroom;

import com.siesque.rilipackcore.block.RilipackcoreBlocks;
import com.siesque.rilipackcore.block.multiblock.FilterBlock;
import com.siesque.rilipackcore.multiblock.controller.BaseMultiblockController;
import com.siesque.rilipackcore.multiblock.pattern.BlockPattern;
import com.siesque.rilipackcore.multiblock.pattern.FactoryBlockPattern;
import com.siesque.rilipackcore.multiblock.pattern.Predicates;
import com.siesque.rilipackcore.multiblock.pattern.TraceabilityPredicate;
import com.siesque.rilipackcore.multiblock.pattern.util.RelativeDirection;
import com.siesque.rilipackcore.tag.RilipackcoreTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanroomController extends BaseMultiblockController {
    private static final int CLEAN_AMOUNT_THRESHOLD = 95;
    // Dimensions are inclusive
    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 10;
    private static final int MIN_WALL_HEIGHT = 3;
    private static final int MAX_WALL_HEIGHT = 7;

    protected int lDist = (MIN_SIZE - 1) / 2;
    protected int rDist = (MIN_SIZE - 1) / 2;
    protected int bDist = (MIN_SIZE - 1) / 2;
    protected int fDist = (MIN_SIZE - 1) / 2;
    protected int hDist = MIN_WALL_HEIGHT;
    protected CleanroomType cleanroomType = null;
    protected int cleanAmount;

    public CleanroomController(Object blockEntity) {
        super((BlockEntity) blockEntity);
        this.cleanAmount = 0;
    }

    @Override
    public BlockPattern getPattern() {
        if (getLevel() != null) updateStructureDimensions();

        // Ensure minimums for pattern generation to avoid crashes if detection fails but pattern is still requested
        int safeL = Math.max(lDist, (MIN_SIZE - 1) / 2);
        int safeR = Math.max(rDist, (MIN_SIZE - 1) / 2);
        int safeB = Math.max(bDist, (MIN_SIZE - 1) / 2);
        int safeF = Math.max(fDist, (MIN_SIZE - 1) / 2);
        int safeH = Math.max(hDist, MIN_WALL_HEIGHT);

        String[] floorLayer = buildFloorLayer(safeL, safeR, safeF, safeB);
        String[][] wallLayers = buildWallLayers(safeL, safeR, safeF, safeB, safeH);
        String[] ceilingLayer = buildCeilingLayer(safeL, safeR, safeF, safeB);

        // Pattern built from floor UP to ceiling
        FactoryBlockPattern pattern = FactoryBlockPattern.start(RelativeDirection.UP, RelativeDirection.FRONT, RelativeDirection.LEFT)
                .aisle(floorLayer);

        for (String[] wallLayer : wallLayers) {
            pattern.aisle(wallLayer);
        }

        return pattern.aisle(ceilingLayer)
                .where('C', Predicates.controller(this))
                .where('F', Predicates.states(getFilterBlocks()))
                .where('S', Predicates.blocks(RilipackcoreBlocks.PLASCRETE_CASING.get(), RilipackcoreBlocks.ENERGY_INPUT_HATCH.get())) // Frame/Borders
                .where('W', Predicates.blocks(RilipackcoreBlocks.PLASCRETE_CASING.get(), RilipackcoreBlocks.ENERGY_INPUT_HATCH.get()).or(Predicates.doors(RilipackcoreTags.Blocks.CLEANROOM_DOORS))) // Walls
                .where('E', Predicates.blocks(RilipackcoreBlocks.PLASCRETE_CASING.get(), RilipackcoreBlocks.ENERGY_INPUT_HATCH.get())) // Floor
                .where(' ', innerPredicate())
                .build();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();

        FilterBlock filterBlock = getMultiblockState().matchContext().get("filterBlock", FilterBlock.class);
        if (filterBlock != null) {
            this.cleanroomType = filterBlock.getCleanroomType();
        } else {
            this.cleanroomType = CleanroomType.CLEANROOM;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.cleanAmount = 0;
        this.cleanroomType = null;
    }

    private void updateStructureDimensions() {
        Level world = getLevel();
        if (world == null) return;

        Direction front = getFrontFacing();
        Direction back = front.getOpposite();
        Direction left = front.getCounterClockWise();
        Direction right = left.getOpposite();

        BlockPos.MutableBlockPos lPos = getPos().mutable();
        BlockPos.MutableBlockPos rPos = getPos().mutable();
        BlockPos.MutableBlockPos fPos = getPos().mutable();
        BlockPos.MutableBlockPos bPos = getPos().mutable();

        lDist = rDist = bDist = fDist = hDist = 0;

        // Scan Horizontally (Ceiling layer)
        // We look for the casing border. Inside should be filters.
        for (int i = 1; i <= MAX_SIZE / 2 + 1; i++) {
            if (lDist == 0 && isBlockBorder(world, lPos, left)) lDist = i;
            if (rDist == 0 && isBlockBorder(world, rPos, right)) rDist = i;
            if (bDist == 0 && isBlockBorder(world, bPos, back)) bDist = i;
            if (fDist == 0 && isBlockBorder(world, fPos, front)) fDist = i;
        }

        int width = lDist + rDist + 1;
        int depth = fDist + bDist + 1;

        if (width < MIN_SIZE || width > MAX_SIZE || depth < MIN_SIZE || depth > MAX_SIZE) {
            return; // Invalid dimensions
        }

        // Check for rough centering (difference of at most 1)
        if (Math.abs(lDist - rDist) > 1 || Math.abs(fDist - bDist) > 1) {
            return; // Not centered
        }

        // Scan Vertically (Downwards)
        BlockPos.MutableBlockPos hPos = getPos().mutable();

        for (int i = 1; i <= MAX_WALL_HEIGHT + 2; i++) {
            // We are at ceiling level. Move down `i` blocks.
            // If i=1, we are at first wall block.
            // We need to find the floor.
            BlockState state = world.getBlockState(hPos.move(Direction.DOWN));
            if (isBlockFloor(state)) {
                hDist = i - 1; // Height of the wall gap
                break;
            }
        }

        if (hDist < MIN_WALL_HEIGHT || hDist > MAX_WALL_HEIGHT) {
            hDist = 0; // Invalid height
        }
    }

    private boolean isBlockBorder(Level world, BlockPos.MutableBlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos.move(direction));
        return state.getBlock() == RilipackcoreBlocks.PLASCRETE_CASING.get() || state.getBlock() == RilipackcoreBlocks.ENERGY_INPUT_HATCH.get();
    }

    private boolean isBlockFloor(BlockState state) {
        return state.getBlock() == RilipackcoreBlocks.PLASCRETE_CASING.get() || state.getBlock() == RilipackcoreBlocks.ENERGY_INPUT_HATCH.get();
    }

    private String[] buildFloorLayer(int l, int r, int f, int b) {
        // Floor is solid 'E'
        String[] layer = new String[f + b + 1];
        Arrays.fill(layer, "E".repeat(Math.max(0, l + r + 1)));
        return layer;
    }

    private String[][] buildWallLayers(int l, int r, int f, int b, int h) {
        String[][] layers = new String[h][f + b + 1];
        for (int k = 0; k < h; k++) {
            for (int j = 0; j < f + b + 1; j++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < l + r + 1; i++) {
                    // Walls are on the border
                    if (i == 0 || i == l + r || j == 0 || j == f + b) {
                        sb.append('W');
                    } else {
                        sb.append(' '); // Air inside
                    }
                }
                layers[k][j] = sb.toString();
            }
        }
        return layers;
    }

    private String[] buildCeilingLayer(int l, int r, int f, int b) {
        String[] layer = new String[f + b + 1];
        for (int j = 0; j < layer.length; j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < l + r + 1; i++) {
                if (i == 0 || i == l + r || j == 0 || j == f + b) {
                    sb.append('S'); // Border
                } else if (i == l && j == f) { // This depends on how relative coordinates work, but usually center matches controller
                    sb.append('C'); // Controller
                } else {
                    sb.append('F'); // Filter
                }
            }
            layer[j] = sb.toString();
        }
        return layer;
    }

    private TraceabilityPredicate innerPredicate() {
        return Predicates.custom(state -> true, null);
    }

    private BlockState getCasingState() {
        return RilipackcoreBlocks.PLASCRETE_CASING.get().defaultBlockState();
    }

    private BlockState[] getFilterBlocks() {
        Block block1 = RilipackcoreBlocks.CLEANROOM_FILTER.get();
        Block block2 = RilipackcoreBlocks.CLEANROOM_FILTER_STERILE.get();

        return new BlockState[]{
                block1.defaultBlockState(),
                block2.defaultBlockState(),
        };
    }

    public void adjustCleanAmount(int amount) {
        this.cleanAmount = Mth.clamp(this.cleanAmount + amount, 0, 100);
    }

    public boolean isClean() {
        return this.cleanAmount >= CLEAN_AMOUNT_THRESHOLD;
    }

    @Override
    public void save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("lDist", lDist);
        tag.putInt("rDist", rDist);
        tag.putInt("fDist", fDist);
        tag.putInt("bDist", bDist);
        tag.putInt("hDist", hDist);
        tag.putInt("cleanAmount", cleanAmount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.lDist = tag.getInt("lDist");
        this.rDist = tag.getInt("rDist");
        this.fDist = tag.getInt("fDist");
        this.bDist = tag.getInt("bDist");
        this.hDist = tag.getInt("hDist");
        this.cleanAmount = tag.getInt("cleanAmount");
    }

    @Override
    public List<Component> getDisplayText() {
        List<Component> text = new ArrayList<>();

        if (isFormed()) {
            if (cleanroomType != null) {
                text.add(Component.translatable(cleanroomType.getTranslationKey()));
            }

            if (isClean()) {
                text.add(Component.translatable("rilipackcore.multiblock.cleanroom.clean_state"));
            } else {
                text.add(Component.translatable("rilipackcore.multiblock.cleanroom.dirty_state"));
            }

            text.add(Component.translatable("rilipackcore.multiblock.cleanroom.clean_amount", cleanAmount));
            text.add(Component.translatable("rilipackcore.multiblock.dimensions",
                    lDist + rDist + 1, hDist + 2, fDist + bDist + 1));
        } else {
            text.add(Component.translatable("rilipackcore.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));

            if (getMultiblockState() != null && getMultiblockState().errorMessage() != null) {
                text.add(getMultiblockState().errorMessage());
            }
        }

        return text;
    }
}