/**
 * List of available Common cards Cardshifter "Mythos" mod.
 * The intention of Common cards is that a player will be able to select from these cards regardless of faction chosen.
 * This is not implemented as of 2015-06-18 and is planned to be implemented during the 0.7 milestone
 * See: https://github.com/Cardshifter/Cardshifter/issues/175
 * @author https://github.com/Phrancis
 * @author https://github.com/jay1148
 */

package mythos

//// CREATURES

card("Swordsman") {
    creature "Common"
    flavor "Armed with a sharp sword."
    maxInDeck 5
    health 3
    sickness 1
    manaCost 5
    attack 3
}
card("Pikeman") {
    creature "Common"
    flavor "Armed with a long armor-piercing pike."
    maxInDeck 5
    health 2
    sickness 1
    manaCost 5
    attack 4
}
card("Archer") {
    creature "Common"
    flavor "Fires arrows from a distance."
    maxInDeck 5
    health 2
    sickness 1
    manaCost 5
    attack 3
    denyCounterAttack() // taunt
}
card("Longbowman") {
    creature "Common"
    flavor "Fires devastating arrows from a very long distance."
    health 2
    sickness 1
    manaCost 8
    attack 5
}
card("Defender") {
    creature "Common"
    flavor "Wields a large shield to protect troops."
    health 6
    sickness 1
    manaCost 10
    attack 1
    noAttack()
    taunt()
}
card("Assassin") {
    creature "Common"
    flavor "Strikes with speed and stealth."
    health 2
    sickness 0
    manaCost 10
    attack 6
    denyCounterAttack() // taunt
}
card("Spy") {
    creature "Common"
    flavor "Weakens the enemy by revealing their secrets."
    maxInDeck 2
    health 4
    sickness 1
    manaCost 10
    attack 0
    noAttack()
    // Opponent cards are -1/-1 while this card is on Battlefield
    whilePresent {
        change HEALTH, ATTACK by -1 withPriority 1 onCards {
            creature true
            ownedBy 'opponent'
            zone 'Battlefield'
        }
    }
}
card("Slingman") {
    creature "Common"
    flavor "Slings stones to break enemy weapons."
    maxInDeck 2
    health 3
    sickness 1
    manaCost 10
    attack 4
    // Opponent cards are -2/-0 while this card is on Battlefield
    whilePresent {
        change ATTACK by -2 withPriority 1 onCards {
            creature true
            ownedBy 'opponent'
            zone 'Battlefield'
        }
    }
}
card("Healer") {
    creature "Common"
    flavor "Supports troops by providing healing."
    maxInDeck 2
    health 5
    sickness 1
    manaCost 10
    attack 0
    noAttack()
    // Own cards have +0/+2 while this card is on Battlefield
    whilePresent {
        change HEALTH by 2 withPriority 1 onCards {
            creature true
            ownedBy 'you'
            zone 'Battlefield'
        }
    }
}

// card('Shaman') {}

// card('Skeleton') {}

// card('Zombie') {}

// card('Snake') {}

// card('Spider') {}

// card('Eagle') {}

// card('Wolf') {}

// card('Bear') {}

// card('Holy Man') {}

//// SPELLS

card('Tree of Life') {
    manaCost 10
    // Player gains +5 health
    spell {}
    afterPlay { heal 5 to "you" }
}

card("Spring of Rejuvenation") {
    manaCost 5
    // Target unit gains +3 health
    spell {
        targets 1 {
            creature true
            ownedBy "you"
            zone "Battlefield"
        }
    }
    afterPlay {
        heal 1 to targets
    }
}

/*
One last chance – Spell
Return target creature from the graveyard to the field for one turn, its effects is negated. 
Mana – 5
*/

/*
card('Resurrection') {
    manaCost 10
    // Return target unit from the graveyard to the field. 
    // Player gains +3 health
    spell {
        targets 1 {
            creature true
            ownedBy "you"
            zone "Discard"
        }
    }
    afterPlay {
        change zone from "Discard" to "Battlefield" // #212
        heal 3 to "you"
    }
}
*/

/*
card('False Idol') {
    manaCost 5
    // Destroy a unit your opponent controls with the lowest attack.
    spell {
        targets 1 {
            creature true
            ownedBy "opponent"
            zone "Battlefield"
            // withLowest ATTACK // #289
        }
    }
    afterPlay {
        set HEALTH to 0 to { targets() }
    }
}
*/

/*
card('Eternal Rest') {
    manaCost 20
    // Destroy a unit your opponent controls with the highest attack.
    spell {
        targets 1 {
            creature true
            ownedBy "opponent"
            zone "Battlefield"
            withHighest ATTACK // #289
        }
    }
    afterPlay {
        set HEALTH to 0 to { targets() }
    }
}
*/

/*
card('Not fit for war') {
    spell {
        targets 1 {
            creature true
            ownedBy "opponent"
            zone "Battlefield"
        }
    }
    afterPlay {
        change zone from "Battlefield" to "Hand" to targets // #290
    }
}
*/

/*
Gift from the gods – Spell
Increase your Mana pool by 20.
Mana - 10
*/

/*
Sacrificial Decree – Spell
Opponent discards a card of his choice. 
Mana - 10
*/
