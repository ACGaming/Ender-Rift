package gigaherz.enderRift.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;

public interface IBetterSidedInventory
{
    default IInventory getInventoryForSide(EnumFacing face)
    {
        if (this instanceof ISidedInventory)
            return InventoryFromSlots.create((IInventory)this, ((ISidedInventory)this).getSlotsForFace(face));
        return null;
    }

}