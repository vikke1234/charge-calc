package com.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("Charge Calc")
public interface ChargeConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "charges",
		name = "Default",
		description = "How many charges should be subtracted from the total amount when filling"
	)
	default int amount()
	{
		return 5;
	}

	@ConfigItem(
			position = 1,
			keyName = "inferno",
			name = "Inferno override",
			description = "Set to > 0 in order to enable"
	)
	default int infernoAmount()
	{
		return 0;
	}
	@ConfigItem(
			position = 2,
			keyName = "colo",
			name = "Colosseum override",
			description = "Set to > 0 in order to enable"
	)
	default int coloAmount()
	{
		return 15;
	}

	@ConfigItem(
			position = 3,
			keyName = "cox",
			name = "CoX override",
			description = "Set to > 0 in order to enable"
	)
	default int coxAmount()
	{
		return 5;
	}

	@ConfigItem(
			position = 4,
			keyName = "tob",
			name = "ToB override",
			description = "Set to > 0 in order to enable"
	)
	default int tobAmount()
	{
		return 20;
	}

	@ConfigItem(
			position = 5,
			keyName = "toa",
			name = "ToA override",
			description = "Set to > 0 in order to enable"
	)
	default int toaAmount()
	{
		return 5;
	}

	@ConfigItem(
			position = 6,
			keyName = "fillKeybind",
			name = "Fill keybind",
			description = "DOES NOT SUPPORT MODIFIERS"
	)
	default Keybind fillBind()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 6,
			keyName = "subKeybind",
			name = "Subtract keybind",
			description = "DOES NOT SUPPORT MODIFIERS"
	)
	default Keybind subBind()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 6,
			keyName = "enterKeybind",
			name = "Enter keybind",
			description = "DOES NOT SUPPORT MODIFIERS"
	)
	default Keybind enterBind()
	{
		return Keybind.NOT_SET;
	}
}
