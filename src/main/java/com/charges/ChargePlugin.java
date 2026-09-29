package com.charges;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.events.VarClientIntChanged;
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

	@Inject
	private ChargeConfig config;

	private ChargeCalc set_amount;



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
		if (event.getIndex() != VarClientID.MESLAYERMODE || client.getVarcIntValue(VarClientID.MESLAYERMODE) != 7) {
			return;
		}

		clientThread.invokeLater(() -> {
			set_amount = new ChargeCalc(client.getWidget(InterfaceID.Chatbox.MES_LAYER), client);
			String title = client.getWidget(InterfaceID.Chatbox.MES_TEXT).getText();
			Pattern title_pattern = Pattern.compile("How many (charges|scales) (do|would) you (like|wish|want) to (add|apply|use)?");
			Matcher title_matcher = title_pattern.matcher(title);
			if (title_matcher.find()) {
				Pattern pattern = Pattern.compile("(\\d+(,\\d+)*)?\\)");
				Matcher matcher = pattern.matcher(title);
				if (matcher.find()) {
					int amount = Integer.parseInt(matcher.group(1).replace(",", ""));
					set_amount.showWidget(config, amount);
				} else {
					log.warn("Could not find pattern");
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
