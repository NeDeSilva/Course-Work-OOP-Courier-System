import ui.*;
import Database;
import BusinessLogic;
import java.util.concurrent.CompletableFuture;

class App{
	public static void main(String[] args){
        CompletableFuture<Void> dbTask = CompletableFuture.runAsync(()-> {DatabaseManager.connect();});
        CompletableFuture.allOf(dbTask).join();
        ui.UIManage();
}
}
