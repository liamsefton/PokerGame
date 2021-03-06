import java.util.Arrays;
public class Player{
    String name;
    double balance;
    Hand hand;
    double current_bet;
    boolean out_this_round;
    boolean standing;

    public Player(String name, double balance, int[] hand){
        this.name = name;
        this.balance = balance;
        this.hand = new Hand(hand);
        out_this_round = false;
        current_bet = 0;
        standing = false;
    }

    public Player(String name, double balance){
        this.name = name;
        this.balance = balance;
        out_this_round = false;
    }

    public void bet(double amount){
        current_bet += amount; //add amount to current bet
        if(current_bet > balance){
            System.out.println("You bet more money than you have, going all in."); //betting over counts as all in
            current_bet = balance;
        }
    }

    public void fold(){ //fold() is just a proxy for lose(), for naming purposes
        lose();
    }

    public void call(double call_amount){ //same idea as fold()
        bet(call_amount);
    }

    public void win(double winnings){ //takes arguement to specify the winnings for different payout levels
        System.out.println(name + " won " + Double.toString(winnings) + " with a hand of " + Arrays.toString(hand.getHandString()));
        balance += winnings;
        current_bet = 0;
        out_this_round = true;
    }

    public void lose(){ //subtracts current bet from balance
        System.out.println(name + " lost " + Double.toString(current_bet) + " with a hand of " + Arrays.toString(hand.getHandString()));
        if(current_bet > balance)
            balance = 0;
        else
            balance -= current_bet;
        current_bet = 0;
        out_this_round = true;
    }

    public void tie(){ //balance doesnt change, bet goes back to 0
        System.out.println(name + " tied with a hand of " + Arrays.toString(hand.getHandString()));
        current_bet = 0;
        out_this_round = true;
    }
}