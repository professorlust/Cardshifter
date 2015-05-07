package com.cardshifter.server.messages;

import java.util.Arrays;

import com.cardshifter.server.commands.CommandContext;
import com.cardshifter.server.model.Command;
import com.cardshifter.server.utils.export.DataExportCommand;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import com.cardshifter.api.CardshifterConstants;
import com.cardshifter.core.game.FakeClient;
import com.cardshifter.core.game.ServerGame;
import com.cardshifter.server.main.ServerMain;
import com.cardshifter.server.model.MainServer;
import com.cardshifter.server.model.Server;

public class ExportTest {

	@Test
	public void test() {
		PropertyConfigurator.configure(ServerMain.class.getResourceAsStream("log4j.properties"));
		
		Server server = new MainServer().start();
		
		ServerGame game = server.createGame(CardshifterConstants.VANILLA);
        FakeClient fakeClient = new FakeClient(server, e -> {});
		game.start(Arrays.asList(fakeClient, new FakeClient(server, e -> {})));
		DataExportCommand exporter = new DataExportCommand();
        DataExportCommand.DataExportParameters params = new DataExportCommand.DataExportParameters();
        params.gameId = 1;
		exporter.handle(new CommandContext(server, new Command(fakeClient, null), fakeClient), params);
		
		server.stop();
	}
	
}
