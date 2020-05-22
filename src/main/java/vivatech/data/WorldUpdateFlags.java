package vivatech.data;

public class WorldUpdateFlags {
    /**
     * Propagates a change event to surrounding blocks.
     **/
    public static final int PROPAGATE_CHANGE        = 0b00000001;
    /**
     * Notifies listeners and clients who need to react when the block changes.
     **/
    public static final int NOTIFY_LISTENERS        = 0b00000010;

    /**
     * Applies PROPAGATE_CHANGE and NOTIFY_LISTENERS.
     **/
    public static final int NOTIFY_AND_PROPAGATE    = 0b00000011;
    /**
     * Used in conjunction with NOTIFY_LISTENERS to suppress the render pass on clients.
     **/
    public static final int NO_REDRAW               = 0b00000100;
    /**
     * Forces a synchronous redraw on clients.
     **/
    public static final int REDRAW_ON_MAIN_THREAD   = 0b00001000;
    /**
     * Bypass virtual blockstate changes and forces the passed state to be stored as-is.
     **/
    public static final int FORCE_STATE             = 0b00010000;
    /**
     * Prevents the previous block (container) from dropping items when destroyed.
     **/
    public static final int SKIP_DROPS              = 0b00100000;
    /**
     * Signals that the current block is being moved to a different location, usually because of a piston.
     **/
    public static final int MOVED                   = 0b01000000;
}
