package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Deck deck;
    private Hand hand;
    private Player player;
    private Dealer dealer;
    private Game game;

    @BeforeEach
    void start(){
        deck = new Deck();
        hand = new Hand();
        player = new Player("Test name");
        dealer = new Dealer("Test dealer");
        game = new Game();
    }

    @Test
    void testDeckInitializationAndGetCard() {
        Card c1 = deck.getCard();
        assertNotNull(c1);
        assertNotNull(c1.rank);
        assertNotNull(c1.suit);
    }

    @Test
    void testDeckRefillsWhenEmpty() {
        // remove all 52 cards
        for (int i = 0; i < 52; i++) {
            deck.getCard();
        }
        // next call should recreate the deck
        Card newCard = deck.getCard();
        assertNotNull(newCard);
    }

    @Test
    void testAddCardAndClearHand() {
        Card card = new Card("♠", "A");
        hand.addCard(card);
        assertEquals(1, hand.hand.size());
        hand.clear();
        assertEquals(0, hand.hand.size());
    }

    @Test
    void testSum(){
        hand.addCard(new Card("♣", "5"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSumm();
        assertEquals(12, summ);
    }

    @Test
    void testSumWithAceLess21(){
        hand.addCard(new Card("♣", "A"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSumm();
        assertEquals(18, summ);
    }

    @Test
    void testSumWithAceMore21(){
        hand.addCard(new Card("♣", "A"));
        hand.addCard(new Card("♣", "10"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSumm();
        assertEquals(18, summ);
    }

    @Test
    void testInitializationParticipants() {
        assertEquals("Test name", player.name);
        assertNotNull(player.hand);
        assertEquals("Test dealer", dealer.name);
        assertNotNull(dealer.hand);
    }

    @Test
    void testGameClearHands() {
        game.prepare();
        game.dealStartCards();
        game.clearHands();

        for (Participant p : game.getParticipants()) {
            assertEquals(0, p.hand.hand.size());
        }
    }

    @Test
    void testCardValueCalculation() {
        assertEquals(11, hand.getIntValue(new Card("♠", "A")));
        assertEquals(10, hand.getIntValue(new Card("♠", "K")));
        assertEquals(10, hand.getIntValue(new Card("♠", "Q")));
        assertEquals(10, hand.getIntValue(new Card("♠", "J")));
        assertEquals(5, hand.getIntValue(new Card("♠", "5")));
    }

}