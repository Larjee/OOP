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
        assertNotNull(c1.getRank());
        assertNotNull(c1.getSuit());
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
        assertEquals(1, hand.getHand().size());
        hand.clear();
        assertEquals(0, hand.getHand().size());
    }

    @Test
    void testSum(){
        hand.addCard(new Card("♣", "5"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSum();
        assertEquals(12, summ);
    }

    @Test
    void testSumWithAceLess21(){
        hand.addCard(new Card("♣", "A"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSum();
        assertEquals(18, summ);
    }

    @Test
    void testSumWithAceMore21(){
        hand.addCard(new Card("♣", "A"));
        hand.addCard(new Card("♣", "10"));
        hand.addCard(new Card("♣", "7"));
        int summ = hand.getSum();
        assertEquals(18, summ);
    }

    @Test
    void testInitializationParticipants() {
        assertEquals("Test name", player.getName());
        assertNotNull(player.getHand());
        assertEquals("Test dealer", dealer.getName());
        assertNotNull(dealer.getHand());
    }

    @Test
    void testGameClearHands() {
        game.prepare();
        game.dealStartCards();
        game.clearHands();

        for (Participant p : game.getParticipants()) {
            assertEquals(0, p.getHand().getHand().size());
        }
    }
}