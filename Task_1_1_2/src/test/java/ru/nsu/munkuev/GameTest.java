package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;


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

        dealerField(game).getHand().clear();
    }

    //Тесты на все служебные классы
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

    //Для доступа к приватным полям и перехвата инпут-аутпута
    private PrintStream savedOut;
    private InputStream savedIn;

    @SuppressWarnings("unchecked")
    private List<Player> playersField(Game g){
        try {
            Field f = Game.class.getDeclaredField("players");
            f.setAccessible(true);
            return (List<Player>) f.get(g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Dealer dealerField(Game g){
        try {
            Field f = Game.class.getDeclaredField("dealer");
            f.setAccessible(true);
            return (Dealer) f.get(g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private ByteArrayOutputStream captureOut(){
        savedOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        return out;
    }
    private void restoreIO(){
        if (savedOut != null){
            System.setOut(savedOut);
        }
        if (savedIn != null){
            System.setIn(savedIn);
        }
        savedOut = null;
        savedIn = null;
    }

    //тесты на determineWinners
    @Test
    void determineWinners_PlayerWinsWith21(){
        List<Player> ps = playersField(game);
        Player a = new Player("Alice");
        ps.add(a);

        Dealer d = dealerField(game);

        a.getHand().addCard(new Card("♠", "K"));
        a.getHand().addCard(new Card("♥", "A"));

        d.getHand().addCard(new Card("♣", "9"));
        d.getHand().addCard(new Card("♦", "7"));

        ByteArrayOutputStream out = captureOut();
        game.determineWinners(1);
        String s = out.toString();
        restoreIO();

        assertTrue(s.contains("End of 1 round"));
        assertTrue(s.contains("Alice win! (21 points)"));
        assertFalse(s.contains("Dealer win! (21 points)"));
    }

    @Test
    void determineWinners_DealerWins(){
        List<Player> ps = playersField(game);
        Player p = new Player("Player");
        ps.add(p);

        Dealer d = dealerField(game);

        p.getHand().addCard(new Card("♠", "6"));
        p.getHand().addCard(new Card("♥", "5"));

        d.getHand().addCard(new Card("♣", "A"));
        d.getHand().addCard(new Card("♦", "K"));

        ByteArrayOutputStream out = captureOut();
        game.determineWinners(2);
        String s = out.toString();
        restoreIO();

        assertTrue(s.contains("Dealer win! (21 points)"));
    }

    @Test
    void determineWinners_Tie_PlayerAndDealer20(){
        List<Player> ps = playersField(game);
        Player p = new Player("TieGuy");
        ps.add(p);

        Dealer d = dealerField(game);

        p.getHand().addCard(new Card("♠", "Q"));
        p.getHand().addCard(new Card("♥", "10"));

        d.getHand().addCard(new Card("♣", "10"));
        d.getHand().addCard(new Card("♦", "Q"));

        ByteArrayOutputStream out = captureOut();
        game.determineWinners(3);
        String s = out.toString();
        restoreIO();

        assertTrue(s.contains("TieGuy win! (20 points)"));
        assertTrue(s.contains("Dealer win! (20 points)"));
    }

    //showHands/showHand
    @Test
    void showHands_HidesDealerSecondCard(){
        List<Player> ps = playersField(game);
        Player p = new Player("Viewer");
        p.getHand().addCard(new Card("♣", "A"));
        p.getHand().addCard(new Card("♥", "10"));
        ps.add(p);

        Dealer d = dealerField(game);
        d.getHand().addCard(new Card("♦", "4"));
        d.getHand().addCard(new Card("♠", "5"));

        ByteArrayOutputStream out = captureOut();
        game.showHands(false);
        String s = out.toString();
        restoreIO();

        assertTrue(s.contains("Viewer"));
        assertTrue(s.contains("[?]"));
    }

    @Test
    void showHand_PrintsFullWhenAllowed(){
        Player p = new Player("ShowTest");
        p.getHand().addCard(new Card("♣", "A"));
        p.getHand().addCard(new Card("♥", "10"));

        ByteArrayOutputStream out = captureOut();
        game.showHand(p, true);
        String s = out.toString();
        restoreIO();

        assertTrue(s.contains("ShowTest"));
        assertTrue(s.contains("points"));
    }

    //Эмуляция ввода y/n
    @Test
    void playerTurns_TakesOneCardThenStops(){
        List<Player> ps = playersField(game);
        Player p = new Player("TestPlayer");
        ps.add(p);

        //здесь эмулируем ввод
        String input = "y\nn\n";
        savedIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream out = captureOut();

        game.prepare();
        game.playerTurns();

        String s = out.toString();
        restoreIO();

        assertTrue(p.getHand().getHand().size() >= 1);
        assertTrue(s.toLowerCase().contains("your turn"));
    }

    //Добор дилера до 17
    @Test
    void dealerTurn_DrawsUntilAtLeast17OrBusts(){
        Dealer d = dealerField(game);

        d.getHand().clear();
        d.getHand().addCard(new Card("♦", "10"));
        d.getHand().addCard(new Card("♠", "6"));

        game.prepare();

        ByteArrayOutputStream out = captureOut();
        game.dealerTurn();
        String s = out.toString();
        restoreIO();

        int sum = d.getHand().getSum();
        assertTrue(sum >= 17 || sum > 21);
        assertTrue(s.contains("Dealer's sum"));
    }

}