import java.util.Random;

public class Poker {
	
	private static Random raNum = new Random();
	private static int pos = 0;
	
	
	
	// Displays the players balances
	public static void displayBalance (Player2 a1) {	
		System.out.println(a1.getName() + " balance: " + String.format("%.2f", a1.getBalance()) + "\n");
	}
	
	//Creates a Deck
	public static Card[] newDeck(){
		Card[] cards = new Card[52];
		int cardIndex = 0;
		int ranDeck = raNum.nextInt(2);
		switch(ranDeck){
			case 0:
				for(int j = 1; j <= 13; j++){
					for(int i = 0; i <= 3; i++){
						Card tempCard = new Card(i, j);
						cards[cardIndex++] = tempCard;
					}
				}
				return cards;
			case 1:
				for(int j = 13; j > 0; j--){
					for(int i = 0; i <= 3; i++){
						Card tempCard = new Card(i, j);
						cards[cardIndex++] = tempCard;
					}
				}
				return cards;
			case 2:
				for(int j = 13; j > 0; j--){
					for(int i = 3; i >= 0; i--){
						Card tempCard = new Card(i, j);
						cards[cardIndex++] = tempCard;
					}
				}
				return cards;
		}
		return cards;
	}
	
	//ShufflesDeck
	public static void shuffDeck(Card[] cards){
		pos = 0;
		for(int j = 0; j < 52; j++){
			int randIndex = raNum.nextInt(52);
			Card tempCard = cards[j];
			cards[j] = cards[randIndex];
			cards[randIndex] = tempCard;
		}
	}
	
	//Puts cards into hands of players
	public static void dealHand(Card[] deck, Player2 p1){
		int numCards = 5 - p1.showHand().handSize();
		for(int j = 0; j < numCards; j ++){
			p1.deal(nextCard(deck));
		}
	}
	
	//Discards card and then deals new cards
	public static void discardAndDeal(Card[] deck, Player2 p1){
		p1.discard();
		for(int i = 0; i < p1.showHand().handSize(); i++){
			Card tempCard = new Card(-1,-1);
			p1.showHand().removeCard(tempCard, nextCard(deck));
			
			//p1.deal(nextCard(deck));	
		}
		if(!p1.getName().equalsIgnoreCase("CPU")) {
			System.out.println("*********************************" + "");
			System.out.println(p1.getName() + "r New Cards: ");
			System.out.println(p1.showHand().printHand()+ "");
			System.out.println("**********************************" + "");
		}
	}
	
	// Returns the next card to deal to the player.
	public static Card nextCard(Card[] deck){
		return deck[pos++];
	}
	
	//Shows hand and asks how much to wage
	public static double wager(Player2 player, double minimum){
		if(!player.getName().equalsIgnoreCase("CPU")) {
		System.out.println(player.getName() +"'s Cards: ");
		player.showHand().sortByValue();
			System.out.println(player.showHand().printHand());
		}
		return player.wager(minimum);
	}
}