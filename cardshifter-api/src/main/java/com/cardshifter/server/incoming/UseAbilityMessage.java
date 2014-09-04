package com.cardshifter.server.incoming;

import com.cardshifter.server.abstr.CardMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class UseAbilityMessage extends CardMessage {

	private final int id;
	private final String action;
	private final int gameId;

	@JsonCreator
	public UseAbilityMessage(@JsonProperty("gameId") int gameId, @JsonProperty("id") int id, @JsonProperty("action") String action) {
		super("use");
		this.id = id;
		this.action = action;
		this.gameId = gameId;
	}
	
	public String getAction() {
		return action;
	}
	
	public int getId() {
		return id;
	}
	
	public int getGameId() {
		return gameId;
	}

}
