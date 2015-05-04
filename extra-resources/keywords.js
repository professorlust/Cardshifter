var pgres = Java.type("net.zomis.cardshifter.ecs.usage.PhrancisGame").PhrancisResources;
var keywords = {};
keywords.systems = {};
keywords.systems.gainResource = function (game, data, value) {
    var retriever = com.cardshifter.modapi.resources.ResourceRetriever.forResource(value.res);
    return new com.cardshifter.modapi.phase.GainResourceSystem(value.res, function (entity) {
        return Math.min(1, Math.max(0, value.untilMax - retriever.getFor(entity)));
    });
};
keywords.systems.restoreResources = function (game, data, value) {
    var retriever = com.cardshifter.modapi.resources.ResourceRetriever.forResource(value.res);
    return new com.cardshifter.modapi.phase.RestoreResourcesSystem(value.res, function (entity) {
        if (typeof value.value === 'object') {
            var restoreTo = com.cardshifter.modapi.resources.ResourceRetriever.forResource(value.value.res);
            return restoreTo.getFor(entity);
        } else {
            return value.value;
        }
    });
};
keywords.systems.playFromHand = function (game, data, value) {
    return new com.cardshifter.modapi.cards.PlayFromHandSystem(value);
};
keywords.systems.playEntersBattlefield = function (game, data, value) {
    return new com.cardshifter.modapi.cards.PlayEntersBattlefieldSystem(value);
};
keywords.systems.useCost = function (game, data, value) {
    var retriever = com.cardshifter.modapi.resources.ResourceRetriever.forResource(value.res);
    return new com.cardshifter.modapi.actions.UseCostSystem(value.action, value.res, function (entity) {
        if (typeof value.value === 'object') {
            var restoreTo = com.cardshifter.modapi.resources.ResourceRetriever.forResource(value.value.res);
            return restoreTo.getFor(entity);
        } else {
            return value.value;
        }
    }, function (entity) {
        // who pays
        if (value.whoPays === 'player') {
            return com.cardshifter.modapi.players.Players.findOwnerFor(entity);
        } else if (value.whoPays === 'self') {
            return entity;
        } else throw new Error("unknown value for 'whoPays': " + value.whoPays);
    });
};
keywords.systems.startCards = function (game, data, value) {
    return new com.cardshifter.modapi.cards.DrawStartCards(value);
}

function applyEntity(game, card, entity, keyword) {
    print("applyEntity " + card + ": " + entity);

    for (var property in card) {
        if (card.hasOwnProperty(property)) {
            var value = card[property];
            print("property found: " + property + " with value " + value + " keyword data is " + keyword[property]);
            if (keyword[property] === undefined) {
                print("keyword " + property + " is undefined");
                throw new Error("property " + property + " was found but is not a declared keyword");
            }
            keyword[property].call(null, entity, card, value);
        }
    }

}


function applyCardKeywords(game, zone, data) {

    for (var i = 0; i < data.cards.length; i++) {
        var card = data.cards[i];
        var entity = game.newEntity();
        print("entity is " + entity);
        applyEntity(game, card, entity, keywords.cards);
        zone.addOnBottom(entity);
    }
}

function applyKeywords(game, data) {

    beforeApplyKeywords(game, data);
    applyKeywords(game, data);
    afterApplyKeywords(game, data);

}

function applySystem(game, data, keyword) {
    print("applyEntity " + data);

    for (var property in data) {
        if (data.hasOwnProperty(property)) {
            var value = data[property];
            print("property found: " + property + " with value " + value + " keyword data is " + keyword[property]);
            if (keyword[property] === undefined) {
                print("keyword " + property + " is undefined");
                throw new Error("property " + property + " was found but is not a declared keyword");
            }
            var system = keyword[property].call(null, game, data, value);
            game.addSystem(system);
        }
    }
}

function applySystems(game, data) {
    for (var i = 0; i < data.length; i++) {
        var system = data[i];
        if (system instanceof com.cardshifter.modapi.base.ECSSystem) {
            game.addSystem(system);
        } else {
            applySystem(game, system, keywords.systems);
        }
    }
}
