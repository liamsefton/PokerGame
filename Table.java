import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Table {
	private double dPot;
	private ArrayList<PokerCard> board = new ArrayList<PokerCard>();// create a board array
	private PokerDeck deck;
	private ArrayList<PokerPlayer> Playerlist = new ArrayList<PokerPlayer>();
	private PokerPlayer currentplayer;
	
	public Table(int iPlayercount)
	{
		
		dPot =0;
		deck = new PokerDeck();// create adeck object
		deck.Shuffle();// shuffle the card
		
		for (int i = 0; i<iPlayercount; i++)// get number of players
		{
			Playerlist.add(new PokerPlayer());// create the number of players
		}
		currentplayer = Playerlist.get(0);// get current player
		
		
	}
	
	public void Deal()// deal the card to player. initial cards
	{
		for (PokerPlayer player: Playerlist)
		{
			player.Addcard(deck.Draw());
			player.Addcard(deck.Draw());
		}
	}
	public void Drawcard()// draw card from the deck
	{
		board.add(deck.Draw());
	}

	public ArrayList<PokerCard> getTable() {//get the 3 cards on the table 
		return board;
	}
	
	public void Nextplayer()// nextplayer
	{
		
		if (currentplayer.equals(Playerlist.get(Playerlist.size()-1)) )
		{
			currentplayer = Playerlist.get(0);
		}
		else
		{
			currentplayer = Playerlist.get(Playerlist.indexOf(currentplayer)+1);
		}
		
	}

	public double getPot() {// get the pot value
		return dPot;
	}
	
	
	
	public void Fold()// check for fold or bet
	{
		Scanner scan = new Scanner(System.in);
		currentplayer.print();
		System.out.println(getTable());
		System.out.print("Would you like to bet or fold: ");
		String sYN = scan.next();
		if (sYN.equalsIgnoreCase("bet"))
		{
			double dTemppot = currentplayer.Bet();
			dPot+=dTemppot;
		}
		else
		{	
			currentplayer.setHasFolded(true);
			currentplayer.Drophand();
			Playerlist.remove(Playerlist.indexOf(currentplayer));
		}
		
		System.out.println("Current numbers of Players: " + Playerlist.size());
	}
	public boolean folded()// in case of fold
	{
		return currentplayer.isHasFolded();
	}
	
	public int Playersize(){ //return the number of players
		
		return Playerlist.size();
	}
	
	public void Score(){// calculate the score of the players for winnerchecker
		
		for (int i = 0; i< Playerlist.size();i++){
		GetScore(currentplayer);
		currentplayer.print();
		System.out.println(currentplayer.getiScore());
		Nextplayer();
		System.out.println(getTable());
		}
	}
	public int winner(){//returns the winner score
		int iTemp = Playerlist.get(0).getiScore();
		for (int i=0; i< Playerlist.size()-1;i++)
		{
			 
			if (iTemp < Playerlist.get(i+1).getiScore())
			{
				iTemp= Playerlist.get(i+1).getiScore();
			}
		}
		return iTemp;
	}

	public String winnerchecker()// returns the winner number
	{
		int iScoretemp = winner();
		String tempString = "";
		for (int i=0; i< Playerlist.size();i++){
			if (iScoretemp ==Playerlist.get(i).getiScore()){
				tempString =  "Player " + (i+1);
			}
				
		}
		return tempString;
	}
	
	
	public void GetScore(PokerPlayer player){// Calculate the winner. Or conditions to win
		
		ArrayList<PokerCard> alCard = new ArrayList<PokerCard>();
		ArrayList<Integer> NumberList = new ArrayList<Integer>();
		ArrayList<Integer> SuitList = new ArrayList<Integer>();

		alCard.add(player.getCards().get(0));
		alCard.add(player.getCards().get(1));
		alCard.addAll(0, board);
		int tempscore = 0;

		NumberList = getNumber(alCard);
		SuitList = getSuit(alCard);

		player.setiHighCard(NumberList.get(0));

		//Flush
		if (SuitList.get(0) == SuitList.get(1) && SuitList.get(1) == SuitList.get(2) && SuitList.get(2) == SuitList.get(3) && SuitList.get(3) == SuitList.get(4) && SuitList.get(4) == SuitList.get(5)){
			tempscore +=500;
		}

		//Straight
		if (NumberList.get(0) == NumberList.get(1) && NumberList.get(1) == NumberList.get(2) && NumberList.get(2) == NumberList.get(3) && NumberList.get(3) == NumberList.get(4) && NumberList.get(4) == NumberList.get(5)){
			tempscore +=400;

		}

		/*if you have both a straight and a flush then you have a straight flush
		 * The code adds your highest card value to your score so if you have a royal flush then it adds 14 which means you have more score than anyone else/ 
		 */
		if (tempscore > 0){
			tempscore += NumberList.get(0);
		}
		//Four of a kind
		if (tempscore <900){
			if ((SuitList.get(0) == SuitList.get(1) && SuitList.get(1) == SuitList.get(2) && SuitList.get(2) == SuitList.get(3) && SuitList.get(3) == SuitList.get(4)) || (SuitList.get(1) == SuitList.get(2) && SuitList.get(2) == SuitList.get(3) && SuitList.get(3) == SuitList.get(4)&& SuitList.get(4) == SuitList.get(5))){
				tempscore = 700;
				tempscore += NumberList.get(1);
			}
			//Three of a kind
			if (isThree(NumberList) > 0 && tempscore < 700){
				tempscore += 300+ isThree(NumberList);
				//Full House
				if (NumberList.get(0) == NumberList.get(1) && NumberList.get(4) == NumberList.get(5)){
					tempscore += 300;
				}
			}

		}
		//Pair and Two Pair
		if (tempscore < 300 && isTwo(NumberList,0) > 0){
			tempscore = 100 + isTwo(NumberList,0);
			if (isTwo(NumberList,isTwo(NumberList,0)) > 0){
				tempscore += 100;
			}
				
		}
		//High Card
		if (tempscore < 100){
			tempscore = NumberList.get(0);
		}
		player.setiScore(tempscore);
	}

	//test for three pair
	private int isThree(ArrayList<Integer> NumberList){
		for (int numb: NumberList){
			int total = 0;
			for (int CheckNumb: NumberList){
				if (numb == CheckNumb){
					total ++;

				}
			}
			if (total == 3){
				return numb;
			}
			else{
				total = 0;
			}


		}
		return 0;
	}

	//test for two pair and pair
	private int isTwo(ArrayList<Integer> NumberList, int ExcludeNumb){
		for (int numb: NumberList){
			int total = 0;
			for (int CheckNumb: NumberList){
				if (numb == CheckNumb && numb != ExcludeNumb){
					total ++;

				}
			}
			if (total == 2){
				return numb;
			}
			else{
				total = 0;
			}


		}
		return 0;
	}
	//Creates array for the number array
	private ArrayList<Integer> getNumber(ArrayList<PokerCard> cards){
		ArrayList<Integer> NumberList = new ArrayList<Integer>();
		for (PokerCard card : cards){
			NumberList.add(card.getNumber());
		}
		Collections.sort(NumberList);
		Collections.reverse(NumberList);
		return NumberList;

	}
	//Creates array for the suit array.
	private ArrayList<Integer> getSuit (ArrayList<PokerCard> cards){
		ArrayList<Integer> SuitList = new ArrayList<Integer>();
		for (PokerCard card : cards){
			SuitList.add(card.getSuit());
		}
		Collections.sort(SuitList);
		Collections.reverse(SuitList);
		return SuitList;

	}
}
	