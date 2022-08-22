package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.power.impl.ConvSelectPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ConvSelectMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select Amiral";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new ConvButton(ConvEnum.KIZARU));
        buttons.put(4, new ConvButton(ConvEnum.AKAINU));
        buttons.put(6, new ConvButton(ConvEnum.FUJITORA));

        return buttons;
    }

    @Getter
    @AllArgsConstructor
    private class ConvButton extends Button {

        private final ConvEnum convEnum;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(convEnum.getResult().getDisplay())
                    .setName(convEnum.getName())
                    .setLore(convEnum.getDescription())
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

            MUPlayer muPlayer = MUPlayer.get(player);

            RolesType.MURole muRole = muPlayer.getRole();

            if(muRole == null){
                player.sendMessage(ChatUtil.prefix("&cVous n'avez pas de rôle."));
                return;
            }

            Power power = muRole.getPowers().stream()
                    .filter(power1 -> power1 instanceof ConvSelectPower)
                    .findFirst()
                    .orElse(null);

            if(power == null){
                player.sendMessage(ChatUtil.prefix("&cVous ne possédez pas le pouvoir pour faire ceci."));
                return;
            }

            ConvSelectPower convSelectPower = (ConvSelectPower) power;

            Messages.SENGOKU_CONV_SELECT.send(player, new Replacement("<name>", convEnum.getName()));
            convSelectPower.setRolesType(convEnum.getResult());
            player.closeInventory();

        }
    }

    @Getter
    @AllArgsConstructor
    private enum ConvEnum {

        KIZARU("Kizaru", new String[]{ "" }, RolesType.KIZARU),
        AKAINU("Akainu", new String[]{ "" }, RolesType.AKAINU),
        FUJITORA("Fujitora", new String[]{ "" }, RolesType.FUJITORA),

        ;

        private final String name;
        private final String[] description;
        private final RolesType result;

    }
}
