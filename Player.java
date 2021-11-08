public class Player{
    String name;
    double balance;
    Hand hand;
    boolean out_this_round;

    public Player(String name, double balance, int[] hand){
        this.name = name;
        this.balance = balance;
        this.hand = new Hand(hand);
        out_this_round = false;
    }

    public void bet(double amount){
        balance -= amount;
    }

    public void fold(){
        out_this_round = true;
    }

    public void call(double call_amount){
        balance -= call_amount;
    }
}