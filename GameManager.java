public class GameManager {
    public Player[] players;
    public int currentPlayer;
    public int currentRound;
    public int gameType;

    public GameManager(int numPlayers, int gameType, String playerName){
        players = new Player[numPlayers];
        players[0] = new HumanPlayer(playerName, 10000);
        for(int i = 1; i < numPlayers; i++){
            players[i] = new ComputerPlayer("CPU" + Integer.toString(i), 10000);
        }
        currentPlayer = 0;
        currentRound = 1;
        this.gameType = gameType;
    }

    public void dealRound(int gameType){
        if(gameType == 0){
            for(int i = 0; i < players.length; i++){
                players[i].hand = new Hand()
            }
        }
    }
}
