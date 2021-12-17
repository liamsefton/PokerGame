public class PokerCard {
	private int cardSuit, cardNumber;

	public PokerCard()
	{
		cardSuit = 0;
		cardNumber = 0;
	}
	public PokerCard(int tempsuit, int tempnumber)
	{
		setSuit(tempsuit);
		setNumber(tempnumber);
	}
	//getters and setters
	public int getSuit() {
		return cardSuit;
	}

	public void setSuit(int cardSuit) {
		this.cardSuit = cardSuit;
	}

	public int getNumber() {
		return cardNumber;
	}

	public void setNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	//tostring to print
	public String toString()
	{
		String pNumber = "";
		if (cardNumber == 2)
			pNumber = "2";
		if (cardNumber == 3)
			pNumber = "3";
		if (cardNumber == 4)
			pNumber = "4";
		if (cardNumber == 5)
			pNumber = "5";
		if (cardNumber == 6)
			pNumber = "6";
		if (cardNumber == 7)
			pNumber = "7";
		if (cardNumber == 8)
			pNumber = "8";
		if (cardNumber == 9)
			pNumber = "9";
		if (cardNumber == 10)
			pNumber = "10";
		
		if (cardNumber == 11)
			pNumber = "J";
		if (cardNumber == 12)
			pNumber = "Q";
		if (cardNumber == 13)
			pNumber = "K";
		if (cardNumber == 14)
			pNumber = "A";
		
		String pSuit = "";
		if (cardSuit == 1)
			pSuit = "♤";
		if (cardSuit == 2)
			pSuit = "♧";
		if (cardSuit == 3)
			pSuit = "♡";
		if (cardSuit == 4)
			pSuit = "♢";
		
        return "[" + pNumber + " of " + pSuit + "]";

	}
	
	
}