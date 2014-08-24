package com.cardshifter.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class Card {
	public final LuaTable data = LuaValue.tableOf();
	
	private final Map<String, Action> actions = new HashMap<>();
	
	private Optional<Zone> currentZone;
	private final Game game;
	
	Card(final Zone currentZone) {
		this.currentZone = Optional.of(currentZone);
		this.game = currentZone.getGame();
	}
	
	public Zone getZone() {
		return currentZone.get();
	}
	
	public boolean hasZone() {
		return currentZone.isPresent();
	}
	
	public Action addAction(final String name, final LuaValue actionAllowed, final LuaValue actionPerformed) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(actionAllowed, "actionAllowed");
		Objects.requireNonNull(actionPerformed, "actionPerformed");
		Action action = new Action(this, name, actionAllowed, actionPerformed);
		actions.put(name, action);
		return action;
	}
	
	public Player getOwner() {
		if (!currentZone.isPresent()) {
			throw new IllegalStateException("Card is not inside a zone: " + this);
		}
		return currentZone.get().getOwner();
	}
	
	public Map<String, Action> getActions() {
		return actions;
	}
	
	public Action getAction(final String name) {
		return actions.get(Objects.requireNonNull(name, "name"));
	}
	
	public void destroy() {
		setZoneInternal(null);
	}
	
	/**
	 * Move this card to the top of another zone
	 * 
	 * @param destination Zone to move to
	 */
	public void moveToTopOf(final Zone destination) {
		moveToZoneInternal(Objects.requireNonNull(destination, "destination"), true);
	}
	
	/**
	 * Move this card to the bottom of another zone
	 * 
	 * @param destination Zone to move to
	 */
	public void moveToBottomOf(final Zone destination) {
		moveToZoneInternal(Objects.requireNonNull(destination, "destination"), false);
	}
	
	private void moveToZoneInternal(final Zone destination, final boolean top) {
		setZone(destination, zone -> {
			Consumer<Card> consumer = (top) 
				? zone.getCards()::addFirst 
				: zone.getCards()::addLast;
			consumer.accept(this);
		});
	}
	
	private void setZoneInternal(final Zone destination) {
		setZone(destination, zone -> {});
	}
	
	private void setZone(final Zone destination, final Consumer<Zone> zoneConsumer) {
		Objects.requireNonNull(zoneConsumer, "zoneConsumer");
		
		Zone resultDestination = currentZone.get().getGame().getEvents().zoneMove(this, currentZone.get(), destination);
		currentZone.get().getCards().remove(this);
		zoneConsumer.accept(destination);
		
		this.currentZone = Optional.ofNullable(resultDestination);
	}

	public Game getGame() {
		return game;
	}
}