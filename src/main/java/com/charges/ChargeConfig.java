package com.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Charge Calc")
public interface ChargeConfig extends Config
{
	@ConfigItem(
		keyName = "charges",
		name = "Remove",
		description = "How many charges should be subtracted from the total amount when filling"
	)
	default int amount()
	{
		return 5;
	}
}
