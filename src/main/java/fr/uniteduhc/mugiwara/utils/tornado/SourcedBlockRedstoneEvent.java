package fr.uniteduhc.mugiwara.utils.tornado;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockRedstoneEvent;

public class SourcedBlockRedstoneEvent extends BlockRedstoneEvent {

    private static final HandlerList handlers = new HandlerList();
    protected final Block source;

    public SourcedBlockRedstoneEvent(Block source, Block block, int old, int n) {

        super(block, old, n);
        this.source = source;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Block getSource() {

        return source;
    }

    public boolean hasChanged() {

        return getOldCurrent() != getNewCurrent();
    }

    public boolean isMinor() {

        return !hasChanged() || wasOn() == isOn();
    }

    public boolean isOn() {

        return getNewCurrent() > 0;
    }

    public boolean wasOn() {

        return getOldCurrent() > 0;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}