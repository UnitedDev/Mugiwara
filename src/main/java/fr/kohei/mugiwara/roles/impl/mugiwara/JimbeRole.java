package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class JimbeRole extends RolesType.MURole {
    public JimbeRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}
