public class HumanPlayer extends Player{
    double insuranceBet;
    boolean has_insurance;

    public HumanPlayer(String name, double balance, int[] hand){
        super(name, balance, hand);
        insuranceBet = 0;
        has_insurance = false;
    }

    public HumanPlayer(String name, double balance){
        super(name, balance);
        insuranceBet = 0;
        has_insurance = false;
    }

    public void betInsurance(double amount){
        insuranceBet = amount;
        if(insuranceBet + current_bet > balance){
            System.out.println(".");
            if(balance - current_bet == 0){
                System.out.println("Are you trying to break my program? You already bet all your money.");
                insuranceBet = 0;
            }
            else{
                System.out.println("Balance exceeded, insurance bet of " + Double.toString(balance-current_bet) + " has been placed.");
                insuranceBet = balance - current_bet;
                has_insurance = true;
            }
        }
        else{
            System.out.println("Insurance bet of " + Double.toString(insuranceBet) + " has been placed.");
            has_insurance = true;
        }
    }

    public void winInsurance(){
        System.out.println("You won " + Double.toString(insuranceBet) + " from your insurance bet!");
        balance += insuranceBet;
        has_insurance = false;
    }

    public void loseInsurance(){
        System.out.println("You lost " + Double.toString(insuranceBet) + " from your insurance bet.");
        balance -= insuranceBet;
        has_insurance = false;
    }


}