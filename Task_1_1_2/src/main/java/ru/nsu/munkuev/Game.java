package ru.nsu.munkuev;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


public final class Game {

    private List<Player> players = new ArrayList<Player>();
    private Dealer dealer = new Dealer("Dealer");
    private Deck deck;

    public void addingPlayers(){

        Scanner scanner = new Scanner(System.in);

        System.out.printf("%s", "\n ♥♦♣♠ Welcome to blackjack! ♥♦♣♠ \n\n");
        System.out.printf("%s", "\nEnter number of players: ");

        int numberOfPlayers = scanner.nextInt();
        scanner.nextLine();

        for(int i = 0; i<numberOfPlayers; i++){
            System.out.printf("%s%d%s", "Enter name of ", i+1, " player: ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }

    }


    public List <Participant> getParticipants(){
        List<Participant> participants = new ArrayList<>();

        for(Player p : players){
            participants.add(p);
        }
        participants.add(dealer);

        return participants;
    }


    public void prepare(){
        deck = new Deck();
        deck.shuffle();
        System.out.printf("%s", "\nDeck created and shuffled\n");
    }


    public void dealStartCards(){
        for(Player player : players){
            player.hand.addCard(deck.getCard());
            player.hand.addCard(deck.getCard());
        }
        dealer.hand.addCard(deck.getCard());
        dealer.hand.addCard(deck.getCard());
    }


    public void showHands(boolean showAllDealerCards){

        //Showing players hands
        for(Player player : players){
            showHand(player, true);
        }

        //Showing dealer hand
        if(showAllDealerCards){
            showHand(dealer, true);
        }
        else{
            showHand(dealer, false);
        }

    }


    public void showHand(Participant participant, boolean showDealer){

        int numberOfCards = participant.hand.hand.size();

        if(showDealer) {
            System.out.printf("%s: ", participant.name);
            for (int i = 0; i < numberOfCards; i++) {
                Card card = participant.hand.hand.get(i);
                System.out.printf("%s%s ", card.getSuit(), card.getRank());
            }
            System.out.printf("(%d points)\n", participant.hand.getSum());
        }
        else{
            System.out.printf("%s: ",participant.name);

            if(numberOfCards>0){
                Card firstCard = participant.hand.hand.get(0);
                System.out.printf("%s%s ", firstCard.getSuit(), firstCard.getRank());
            }
            for(int i = 1; i<numberOfCards;i++){
                System.out.printf("[?]\n");
            }
        }

    }


    public void playerTurns(){

        Scanner scanner = new Scanner(System.in);

        showHands(false);

        for(Player player : players){

            //Telling whose player turn now and showing his cards
            System.out.printf("\n ♥♦♣♠ %s! Your turn! ♥♦♣♠ \n", player.name);

            showHand(player, true);

            //Request
            while(true){

                System.out.printf("Do you wanna take one more card? y/n: ");
                String answer = scanner.nextLine().toLowerCase();

                //If player take card
                if (answer.equals("y")) {
                    player.hand.hand.add(deck.getCard());
                    int numberOfCards = player.hand.hand.size();
                    System.out.printf("%s got %s%s\n", player.name, player.hand.hand.get(numberOfCards - 1).getSuit(), player.hand.hand.get(numberOfCards - 1).getRank());
                    int summ = player.hand.getSum();
                    System.out.printf("%s's sum = %d\n\n", player.name, summ);

                    if (summ > 21){
                        System.out.printf(" ♥♦♣♠ You lose! ♥♦♣♠ \n");
                        break;
                    }
                }
                //If player refuse taking card
                else if(answer.equals("n")){
                    System.out.printf("%s refused to take card\n", player.name);
                    break;
                }
                //If player entered wrong symbols
                else{
                    System.out.printf("\nWrong answer! Use y/n\n");
                }
            }


        }
    }


    public void dealerTurn(){

        System.out.printf("\n ♥♦♣♠ Dealer's turn! ♥♦♣♠ \n");

        //Show dealer's hand and if summ less than 17 picking cards until summ reaches 17
        showHand(dealer, true);

        while(dealer.hand.getSum() < 17){
            dealer.hand.hand.add(deck.getCard());
            int numberOfCards = dealer.hand.hand.size();
            System.out.printf("Dealer got %s%s\n", dealer.hand.hand.get(numberOfCards - 1).getSuit(), dealer.hand.hand.get(numberOfCards - 1).getRank());

            int summ = dealer.hand.getSum();
            System.out.printf("Dealer's sum = %d\n\n", summ);

            if(summ >= 17){
                break;
            }
            else if(summ > 21){
                System.out.printf(" ♥♦♣♠ Dealer lose! ♥♦♣♠ ");
                break;
            }

        }
    }


    public void determineWinners(int round){
        System.out.printf("\n ♥♦♣♠ End of %d round ♥♦♣♠\n", round);
        showHands(true);

        int maxPoints = 0;
        List<String> nameWinners = new ArrayList<>();

        //find max points
        for(Player player : players){
            int curPoints = player.hand.getSum();
            if(curPoints > maxPoints && curPoints <= 21){
                maxPoints = curPoints;
            }
        }
        int dealerPoints = dealer.hand.getSum();
        if(dealerPoints > maxPoints && dealerPoints <= 21){
            maxPoints = dealerPoints;
        }

        //find players with max points
        for(Player player : players){
            if(player.hand.getSum() == maxPoints){
                nameWinners.add(player.name);
            }
        }
        if(dealerPoints == maxPoints){
            nameWinners.add("Dealer");
        }

        System.out.printf("\n");
        for(String name : nameWinners){
            System.out.printf("%s win! (%d points)\n", name, maxPoints);
        }

    }


    public void clearHands(){
        for(Player player : players){
            player.hand.clear();
        }
        dealer.hand.clear();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Game game = new Game();
        game.addingPlayers();
        System.out.printf("%s","\n ♥♦♣♠ Starting game!!! ♥♦♣♠ \n");

        for(int round = 1; ; round++){
            boolean nextRound = true;

            game.prepare();

            System.out.printf("\n ♥♦♣♠ %d Round ♥♦♣♠ \n", round);

            game.dealStartCards();

            game.playerTurns();

            game.dealerTurn();

            game.determineWinners(round);

            //Checking if user want to continue game
            while(true){
                System.out.printf("\n ♥♦♣♠ Do you wanna continue? y/n ♥♦♣♠ \n");
                String answer = scanner.nextLine();

                if(answer.equals("n") || answer.equals("N")){

                    System.out.printf(" ♥♦♣♠ Good luck! ♥♦♣♠ \n");
                    nextRound = false;
                    break;

                }
                else if(answer.equals("y") || answer.equals("Y")){
                    break;
                }
                else{
                    System.out.printf("Wrong answer! Use y/n\n");
                }
            }
            //If user don't want to continue then break a for
            if (!nextRound){
                break;
            }
            game.clearHands();

        }


    }
}
