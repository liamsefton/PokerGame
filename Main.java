public class Main {
    public static void main(String[] args){
        GameManager test = new GameManager(5, 0, "Liam");
        
        while(!allBroke(test))
            test.dealRound();
    }

    public static boolean allBroke(GameManager gManager){
        boolean ret = true;
        for(int i = 0; i < gManager.players.length; i++){
            if(gManager.players[i].balance > 0)
                ret = false;
        }
        return ret;
    }
}
