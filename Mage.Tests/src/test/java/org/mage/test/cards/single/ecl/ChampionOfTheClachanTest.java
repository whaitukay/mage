package org.mage.test.cards.single.ecl;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

public class ChampionOfTheClachanTest extends CardTestPlayerBase {

    @Test
    public void testBeholdExilesPermanentAndReturnsItWhenChampionLeaves() {
        setStrictChooseMode(true);

        addCard(Zone.HAND, playerA, "Champion of the Clachan");
        addCard(Zone.HAND, playerA, "Terminate");
        addCard(Zone.BATTLEFIELD, playerA, "Kithkin Shielddare");
        addCard(Zone.BATTLEFIELD, playerA, "Kithkin Zealot");
        addCard(Zone.BATTLEFIELD, playerA, "Plateau", 4);
        addCard(Zone.BATTLEFIELD, playerA, "Badlands", 2);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Champion of the Clachan");
        setChoice(playerA, "Kithkin Zealot"); // Behold and exile a controlled Kithkin.

        castSpell(1, PhaseStep.POSTCOMBAT_MAIN, playerA, "Terminate", "Champion of the Clachan");

        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertPermanentCount(playerA, "Champion of the Clachan", 0);
        assertGraveyardCount(playerA, "Champion of the Clachan", 1);
        assertHandCount(playerA, "Kithkin Zealot", 1);
        assertExileCount(playerA, "Kithkin Zealot", 0);
        assertPowerToughness(playerA, "Kithkin Shielddare", 1, 1);
    }

    @Test
    public void testBeholdExilesHandCardAndReturnsItWhenChampionLeaves() {
        setStrictChooseMode(true);

        addCard(Zone.HAND, playerA, "Champion of the Clachan");
        addCard(Zone.HAND, playerA, "Terminate");
        addCard(Zone.HAND, playerA, "Kithkin Zealot");
        addCard(Zone.BATTLEFIELD, playerA, "Plateau", 4);
        addCard(Zone.BATTLEFIELD, playerA, "Badlands", 2);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Champion of the Clachan");
        setChoice(playerA, "Kithkin Zealot"); // Reveal, behold, and exile it from hand.

        castSpell(1, PhaseStep.POSTCOMBAT_MAIN, playerA, "Terminate", "Champion of the Clachan");

        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertPermanentCount(playerA, "Champion of the Clachan", 0);
        assertGraveyardCount(playerA, "Champion of the Clachan", 1);
        assertHandCount(playerA, "Kithkin Zealot", 1);
        assertExileCount(playerA, "Kithkin Zealot", 0);
    }
}
