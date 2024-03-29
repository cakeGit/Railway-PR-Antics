package com.railwayteam.railways.content.custom_bogeys.blocks.gauge.standard.large;

import com.railwayteam.railways.content.custom_bogeys.CRBogeyBlock;
import com.railwayteam.railways.registry.CRBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import net.minecraft.world.phys.Vec3;

public class LargeCreateStyle0120BogeyBlock extends CRBogeyBlock {
    public LargeCreateStyle0120BogeyBlock(Properties props) {
        this(props, CRBogeyStyles.LARGE_CREATE_STYLED_0_12_0, BogeySizes.LARGE);
    }

    protected LargeCreateStyle0120BogeyBlock(Properties props, BogeyStyle defaultStyle, BogeySizes.BogeySize size) {
        super(props, defaultStyle, size);
    }

    //fixme fix offset
    @Override
    public Vec3 getConnectorAnchorOffset() {
        return new Vec3(0, 7 / 32f, 140 / 32f);
    }

    @Override
    public double getWheelPointSpacing() {
        return 6;
    }
}
