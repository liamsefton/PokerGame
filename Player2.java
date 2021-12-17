import java.util.*;

public class Player2 {
	
	private PokerHand playerHand;
	private double balance;
	private String name;
	
	public Player2(String inputName, double balance) {
		this.playerHand = new PokerHand();
		this.name = inputName;
		this.balance = balance;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void deal(Card card){
		playerHand.addCard(card);
	}
	
	public void discard() {
		boolean cardTrue = false;
		Scanner kb = new Scanner(System.in);
		int numCardsDiscard = 0;
		System.out.println("How many cards would you like to change?");
		Card tempCard = new Card();
		String suit = "";
		int suitNum = 0;
		
		if(!this.getName().equalsIgnoreCase("CPU")) {
			numCardsDiscard = kb.nextInt();
			if(numCardsDiscard == 0) {
				System.out.println("You have chose to not change any cards.");
			} else if(numCardsDiscard <= 5 && numCardsDiscard > 0) {
				do {
					do {
						System.out.println("\n" + "Your hand:");
						System.out.println(this.showHand().printHand());
						System.out.println("which card would you like to change?");
						System.out.println("Type the suit and then the value.");
						System.out.println("Type the number for the suit: ");
						System.out.println("0 for ♧");
						System.out.println("1 for ♢");
						System.out.println("2 for ♡");
						System.out.println("3 for ♤");
						suitNum = kb.nextInt();
						System.out.println("\n");
				


						
						if(!(suitNum >= 0 || suitNum <= 3)) {
							do {
								System.out.println("Invalid suit, please enter a valid suit: ");
								suitNum = kb.nextInt();
							}while(!(suitNum >= 0 || suitNum <= 3));
						}
						System.out.println("What is the value of the card?");
						System.out.println("Type the number for the value: ");
						System.out.println("1 for A");
						System.out.println("11 for J");
						System.out.println("12 for Q");
						System.out.println("13 for K");
						tempCard = new Card(suitNum,kb.nextInt());
						System.out.println("\n");
						
						for(int e = 0; e < this.showHand().handSize(); e++) {
							if(this.showHand().returnCard(e).compareTo(tempCard) != 0) {
								cardTrue = false;
							} else {
								cardTrue = true;
								break;
							}
						}
						if(cardTrue == false) {
							System.out.println("The card you have selected is invalid, please try again");
						}
					}while(cardTrue == false);
					cardTrue = false;
					this.playerHand.removeCard(tempCard);
					numCardsDiscard--;
				}while(numCardsDiscard != 0);
			}
			return;
		}
		
		
		
		//For Computer
		if(playerHand.hasStraight() == true && playerHand.hasFlush() == true) {
			// numCardsDiscard = 0;
		}
		else if(playerHand.hasFourOfAKind() == true) {
			// numCardsDiscard = 1;
			this.playerHand.sortByValue();
			if(this.playerHand.getCardValueAt(0) == this.playerHand.getCardValueAt(2)) {
				tempCard = new Card(this.playerHand.getCardValueAt(4), this.playerHand.getCardValueAt(4));
				this.playerHand.removeCard(tempCard);
			} else {
				tempCard = new Card(this.playerHand.getCardValueAt(0), this.playerHand.getCardValueAt(0));
				this.playerHand.removeCard(tempCard);
			}
		}
		else if(playerHand.hasFullHouse() == true) {
			// numCardsDiscard = 2;
			tempCard = new Card(this.playerHand.getCardValueAt(0), this.playerHand.getCardValueAt(0));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(4), this.playerHand.getCardValueAt(4));
			this.playerHand.removeCard(tempCard);
		}
		else if(playerHand.hasFlush() == true) {
			//numCardsDiscard = 5;
			this.playerHand.clear();
		}
		else if(playerHand.hasStraight() == true) {
			//numCardsDiscard = 5;
			this.playerHand.clear();
		}
		else if(playerHand.hasTriplet() == true) {
			//numCardsDiscard = 2;
			tempCard = new Card(this.playerHand.getCardValueAt(0), this.playerHand.getCardValueAt(0));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(1), this.playerHand.getCardValueAt(1));
			this.playerHand.removeCard(tempCard);
		}
		else if(playerHand.numPairs() == 2) {
			//numCardsDiscard = 3;
			tempCard = new Card(this.playerHand.getCardValueAt(0), this.playerHand.getCardValueAt(0));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(2), this.playerHand.getCardValueAt(2));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(4), this.playerHand.getCardValueAt(4));
			this.playerHand.removeCard(tempCard);
		}
		else if(playerHand.numPairs() == 1) {
			//numCardsDiscard = 3;
			tempCard = new Card(this.playerHand.getCardValueAt(0), this.playerHand.getCardValueAt(0));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(2), this.playerHand.getCardValueAt(2));
			this.playerHand.removeCard(tempCard);
			tempCard = new Card(this.playerHand.getCardValueAt(4), this.playerHand.getCardValueAt(4));
			this.playerHand.removeCard(tempCard);
		}
		else{
			//numCardsDiscard = 5;
			this.playerHand.clear();
		}
		return;
	}
	
	public double wager(double min) {
		Scanner kb = new Scanner(System.in);
		
		System.out.println("Minimum Bet: " + min);
		System.out.println("How much would you want to bet ");
		
		double wage = min;
		
		if(!this.name.equalsIgnoreCase("CPU")) {
			wage = kb.nextDouble();
			if(wage >= min) {
				balance -= wage;
				return wage;
			} else if (wage < min && this.balance >= min){
				do {
					System.out.println("Bet cannot be lower than the Minimum amount");
					System.out.println("Please enter a valid amount to Bet: ");
					wage = kb.nextDouble();
				}while(wage < min);
				balance -= wage;
				return wage;
			} else if (wage < min && this.balance < min) {
				System.out.println("You have to go all in");
				System.out.println("To go all in, type your full balance (Your balance: " + this.getBalance() +")");
				wage = kb.nextDouble();
				if(wage < min) {
					do{
						System.out.println("Invalid Amount.");
						System.out.println("Put down the correct aomunt to bet: " + this.getBalance());
						wage = kb.nextDouble();
					}while(wage != balance);
					return wage;
				}
			}
		}
		
		
		//for computer
		if(playerHand.hasStraight() == true && playerHand.hasFlush() == true) {
			wage = this.balance;
			this.balance = this.balance - this.balance;
			System.out.println("CPU has chose to Bet: " + wage);
			return wage;
		}
		if(playerHand.hasFourOfAKind() == true) {
			if((this.balance*.7) >= min) {
				wage = this.balance * .7f;
				this.balance -= wage;
				System.out.println("CPU has chose to Bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to Bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.hasFullHouse() == true) {
			if((this.balance*.6) >= min) {
				this.balance = this.balance * .6f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to Bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.hasFlush() == true) {
			if((this.balance*.5) >= min) {
				wage = this.balance * .5f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.hasStraight() == true) {
			if((this.balance) >= min) {
				wage = this.balance * .4f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.hasTriplet() == true) {
			if((this.balance*.3) >= min) {
				wage = this.balance * .3f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.numPairs() == 2) {
			if((this.balance*.2) >= min) {
				wage = this.balance * .2f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		if(playerHand.numPairs() == 1) {
			if((this.balance*.1) >= min) {
				wage = this.balance * .1f;
				this.balance -= wage;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return wage;
			}
			else {
				this.balance -= min;
				System.out.println("CPU has chose to bet: " + String.format("%.2f", wage));
				return min;
			}
		}
		kb.close();
		return -1;
	}
	
	public PokerHand showHand() {
		return playerHand;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void winnings(double amount){
		this.balance += amount;
	}
}