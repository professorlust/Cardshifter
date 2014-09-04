package com.cardshifter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class TargetAction extends UsableAction {

	private final Card card;
	private final LuaValue targetAllowed;
	private Targetable target;

	public TargetAction(Card card, String name, LuaValue actionAllowed, LuaValue targetAllowed, LuaValue actionPerformed) {
		super(name, actionAllowed, actionPerformed);
		this.card = card;
		this.targetAllowed = targetAllowed;
	}

	@Override
	public void perform() {
		if (!this.isValidTarget(target)) {
			throw new IllegalStateException("Not a valid target: " + target);
		}
		this.perform(target);
	}
	
	@Override
	protected LuaValue methodArg() {
		return CoerceJavaToLua.coerce(card);
	}

	@Override
	protected Game getGame() {
		return card.getGame();
	}

	public List<Targetable> findTargets() {
		List<Targetable> targets = new ArrayList<>();
		Game game = getGame();
		for (Targetable target : game.getPlayers()) {
			if (isValidTarget(target)) {
				targets.add(target);
			}
		}
		targets.addAll(game.getZones().stream().flatMap(zone -> zone.getCards().stream()).filter(this::isValidTarget).collect(Collectors.toList()));
		return targets;
	}

	public boolean isValidTarget(Targetable target) {
		return targetAllowed.invoke(CoerceJavaToLua.coerce(card), CoerceJavaToLua.coerce(target), CoerceJavaToLua.coerce(this)).arg1().toboolean();
	}

	private void perform(Targetable target) {
		Game game = getGame(); // stored here in case it is unavailable after action has been performed
		getActionFunction().invoke(CoerceJavaToLua.coerce(card), CoerceJavaToLua.coerce(target), CoerceJavaToLua.coerce(this));
		game.getEvents().callEvent(Events.ACTION_USED, CoerceJavaToLua.coerce(card), CoerceJavaToLua.coerce(this));
	}
	
	@Override
	public String toString() {
		return "{TargetAction " + this.getName() + " on card " + this.card + "}";
	}
	
	public void setTarget(Targetable target) {
		if (!isValidTarget(target)) {
			throw new IllegalArgumentException("Not a valid target: " + target);
		}
		this.target = target;
	}

	@Override
	public int getEntityId() {
		return card.getId();
	}
	
}
