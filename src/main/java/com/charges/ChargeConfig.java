package com.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Charge Calc")
public interface ChargeConfig extends Config
{
	@ConfigItem(
		keyName = "charges",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default int greeting()
	{
		return 10;
	}
}
