import java.util.*;

public class PokerDeck {

	private int [] pokerCards;// pokerCards array
	private int[] pokerSuits;//pokerSuits array

	private ArrayList<PokerCard> deck = new ArrayList<PokerCard>();// create a deck array

	public PokerDeck()
	{
		pokerCards = new int[] {2,3,4,5,6,7,8,9,10,11,12,13,14};//construct
		pokerSuits = new int[] {1,2,3,4};
		for (int i: pokerCards)
		{
			for (int j: pokerSuits)
			{
				PokerCard pCard = new PokerCard(j,i);
				deck.add(pCard);
			}
		}
		
	}
	
	public void Shuffle()// shuffle the pokerCards in the deck
	{
		for (int i = 0; i< 150; i++){
			Random random = new Random();
			PokerCard tempcard = deck.get(random.nextInt(52));
			deck.remove(tempcard);
			deck.add(tempcard);
		}
		
	}
	
	public PokerCard Draw()// draw a pokerCards from the deck
	{
		PokerCard tempcard = deck.get(0);
		deck.remove(tempcard);
		return tempcard;
	}
}