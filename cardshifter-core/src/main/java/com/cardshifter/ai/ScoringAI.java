package com.cardshifter.ai;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.cardshifter.ecs.actions.ActionComponent;
import net.zomis.cardshifter.ecs.actions.ECSAction;
import net.zomis.cardshifter.ecs.base.ECSGame;
import net.zomis.cardshifter.ecs.base.Entity;

public class ScoringAI implements CardshifterAI, ScoreStrategy<Entity, ECSAction> {
	
	private final Random random = new Random(42);
	private final ScoreConfig<Entity, ECSAction> config;
	
	public ScoringAI(ScoreConfigFactory<Entity, ECSAction> config) {
		this.config = config.build();
	}
	
	@Override
	public ECSAction getAction(Entity player) {
		FieldScoreProducer<Entity, ECSAction> prod = new FieldScoreProducer<Entity, ECSAction>(config, this);
		ParamAndField<Entity, ECSAction> best = ScoreUtils.pickBest(prod, player, random);
		if (best != null) {
			return best.getField();
		}
		return null;
	}
	
	@Override
	public boolean canScoreField(ScoreParameters<Entity> params, ECSAction action) {
		return action.isAllowed(params.getParameters());
	}

	@Override
	public Collection<ECSAction> getFieldsToScore(Entity entity) {
		return getAllActions(entity.getGame()).collect(Collectors.toList());
	}
	
	private static Stream<ECSAction> getAllActions(ECSGame game) {
		return game.getEntitiesWithComponent(ActionComponent.class)
			.stream()
			.flatMap(entity -> entity.getComponent(ActionComponent.class)
				.getECSActions().stream());
	}
	
}