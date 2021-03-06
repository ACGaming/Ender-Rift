package gigaherz.enderRift.rift;

import gigaherz.enderRift.EnderRiftMod;
import gigaherz.enderRift.automation.TileAggregator;
import gigaherz.enderRift.plugins.tesla.TeslaControllerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEnderRiftCorner
        extends TileAggregator
{
    TileEnderRift energyParent;

    private Capability teslaConsumerCap;
    private Object teslaConsumerInstance;

    private Capability teslaHolderCap;
    private Object teslaHolderInstance;

    public TileEnderRiftCorner()
    {
        teslaConsumerCap = TeslaControllerBase.CONSUMER.getCapability();
        teslaHolderCap = TeslaControllerBase.HOLDER.getCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        IEnergyStorage buffer = getEnergyBuffer();
        if (capability == CapabilityEnergy.ENERGY)
            return buffer != null;
        if (teslaConsumerCap != null && capability == teslaConsumerCap)
            return buffer != null;
        if (teslaHolderCap != null && capability == teslaHolderCap)
            return buffer != null;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        IEnergyStorage buffer = getEnergyBuffer();
        if (capability == CapabilityEnergy.ENERGY)
            return (T) buffer;
        if (teslaConsumerCap != null && capability == teslaConsumerCap)
        {
            if (teslaConsumerInstance == null && buffer != null)
                teslaConsumerInstance = TeslaControllerBase.CONSUMER.createInstance(getEnergyBuffer());
            return (T) teslaConsumerInstance;
        }
        if (teslaHolderCap != null && capability == teslaHolderCap)
        {
            if (teslaHolderInstance == null && buffer != null)
                teslaHolderInstance = TeslaControllerBase.HOLDER.createInstance(getEnergyBuffer());
            return (T) teslaHolderInstance;
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    public IEnergyStorage getEnergyBuffer()
    {
        return getInternalBuffer();
    }

    @Nullable
    @Override
    public IEnergyStorage getInternalBuffer()
    {
        TileEnderRift parent = getParent();
        return parent != null ? parent.getEnergyBuffer() : null;
    }

    @Override
    protected void lazyDirty()
    {
        // Nothing to do
    }

    @Override
    protected boolean canConnectSide(EnumFacing side)
    {
        return false;
    }

    @Nullable
    public TileEnderRift getParent()
    {
        if (energyParent == null)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() != EnderRiftMod.structure)
                return null;

            TileEntity te = world.getTileEntity(getRiftFromCorner(state, pos));
            if (te instanceof TileEnderRift)
            {
                energyParent = (TileEnderRift) te;
            }
            else
            {
                return null;
            }
        }
        return energyParent;
    }

    private static BlockPos getRiftFromCorner(IBlockState state, BlockPos pos)
    {
        boolean base = state.getValue(BlockStructure.BASE);
        BlockStructure.Corner corner = state.getValue(BlockStructure.CORNER);
        int xParent = pos.getX();
        int yParent = pos.getY() + (base ? 1 : -1);
        int zParent = pos.getZ();
        switch (corner)
        {
            case NE:
                xParent -= 1;
                zParent += 1;
                break;
            case NW:
                xParent += 1;
                zParent += 1;
                break;
            case SE:
                xParent -= 1;
                zParent -= 1;
                break;
            case SW:
                xParent += 1;
                zParent -= 1;
                break;
        }
        return new BlockPos(xParent, yParent, zParent);
    }
}