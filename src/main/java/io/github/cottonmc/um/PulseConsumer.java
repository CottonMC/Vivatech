package io.github.cottonmc.um;

/**
 * Think of pulses as really, *really* slow, variable-length ticks, which slowly waterfall through a machine setup like
 * redstone. In fact, some of the mechanics that drive redstone, like vanilla tick scheduling, drive pulses too.
 * 
 * A pulse consumer can accept these slow ticks, and are expected to either reschedule themselves or sleep until a pulse
 * becomes necessary for other reasons. For instance, a generator that runs out of fuel and finishes burning might sleep
 * until more fuel is inserted, literally not ticking until that happens.
 */
public interface PulseConsumer {
	public void pulse();
}
