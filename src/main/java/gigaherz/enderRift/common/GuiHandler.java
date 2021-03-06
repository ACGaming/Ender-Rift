package gigaherz.enderRift.common;

import gigaherz.enderRift.automation.browser.*;
import gigaherz.enderRift.automation.iface.ContainerInterface;
import gigaherz.enderRift.automation.iface.GuiInterface;
import gigaherz.enderRift.automation.iface.TileInterface;
import gigaherz.enderRift.generator.ContainerGenerator;
import gigaherz.enderRift.generator.GuiGenerator;
import gigaherz.enderRift.generator.TileGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    public static final int GUI_INTERFACE = 0;
    public static final int GUI_GENERATOR = 1;
    public static final int GUI_BROWSER = 2;
    public static final int GUI_BROWSER_CRAFTING = 3;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        switch (id)
        {
            case GUI_INTERFACE:
                if (tileEntity instanceof TileInterface)
                {
                    return new ContainerInterface((TileInterface) tileEntity, player.inventory);
                }
                break;
            case GUI_GENERATOR:
                if (tileEntity instanceof TileGenerator)
                {
                    return new ContainerGenerator((TileGenerator) tileEntity, player.inventory);
                }
                break;
            case GUI_BROWSER:
                if (tileEntity instanceof TileBrowser)
                {
                    return new ContainerBrowser((TileBrowser) tileEntity, player, false);
                }
                break;
            case GUI_BROWSER_CRAFTING:
                if (tileEntity instanceof TileBrowser)
                {
                    return new ContainerCraftingBrowser((TileBrowser) tileEntity, player, false);
                }
                break;
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        switch (id)
        {
            case GUI_INTERFACE:
                if (tileEntity instanceof TileInterface)
                {
                    return new GuiInterface(player.inventory, (TileInterface) tileEntity);
                }
                break;
            case GUI_GENERATOR:
                if (tileEntity instanceof TileGenerator)
                {
                    return new GuiGenerator(player.inventory, (TileGenerator) tileEntity);
                }
                break;
            case GUI_BROWSER:
                if (tileEntity instanceof TileBrowser)
                {
                    return new GuiBrowser(player, (TileBrowser) tileEntity);
                }
                break;
            case GUI_BROWSER_CRAFTING:
                if (tileEntity instanceof TileBrowser)
                {
                    return new GuiCraftingBrowser(player, (TileBrowser) tileEntity);
                }
                break;
        }

        return null;
    }
}
