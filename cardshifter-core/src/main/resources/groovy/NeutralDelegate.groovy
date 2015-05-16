import com.cardshifter.modapi.base.Entity
import com.cardshifter.modapi.cards.ZoneComponent
import com.cardshifter.modapi.phase.PhaseController
import com.cardshifter.modapi.resources.ResourceModifierComponent
import ZoneDelegate

public class NeutralDelegate {
    Entity entity

    def resourceModifier() {
        entity.addComponent(new ResourceModifierComponent());
    }

    def phases() {
        entity.addComponent(new PhaseController())
    }

    def zone(String name, Closure<?> closure) {
        def zone = new ZoneComponent(entity, name)
        entity.addComponent(zone)
        println "Zone $name"
        closure.delegate = new ZoneDelegate(entity: entity, zone: zone)
        closure.call(closure)
    }
    def addCards() {
        println 'Add cards'
    }
}

