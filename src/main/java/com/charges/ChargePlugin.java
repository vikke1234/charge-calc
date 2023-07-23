package com.charges;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.widgets.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Charge Calculator"
)
public class ChargePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	private ChargeCalc set_amount;

	@Inject
	private ChargeConfig config;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onVarClientIntChanged(VarClientIntChanged event)
	{
		if (event.getIndex() != VarClientInt.INPUT_TYPE || client.getVarcIntValue(VarClientInt.INPUT_TYPE) != 7) {
			return;
		}

		clientThread.invokeLater(() -> {
			set_amount = new ChargeCalc(client.getWidget(WidgetInfo.CHATBOX_CONTAINER), client);
			String title = client.getWidget(WidgetInfo.CHATBOX_TITLE).getText();
			if (title.startsWith("How many charges do you want to apply?")) {
				Pattern pattern = Pattern.compile("(\\d+(,\\d+)*)?\\)");
				Matcher matcher = pattern.matcher(title);
				if (matcher.find()) {
					int amount = Integer.parseInt(matcher.group(1).replace(",", ""));
					set_amount.showWidget(config.amount(), amount);
				} else {
					System.out.println("Could not find pattern");
				}
			}
		});
	}

	@Provides
	ChargeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChargeConfig.class);
	}
}
