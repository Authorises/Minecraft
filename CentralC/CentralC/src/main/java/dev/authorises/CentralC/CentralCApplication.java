package dev.authorises.CentralC;

import com.google.gson.Gson;
import dev.authorises.CentralC.model.access.AccessManager;
import dev.authorises.CentralC.model.access.AccessToken;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.map.leaderboard.MapLeaderboardManager;
import dev.authorises.CentralC.model.players.OnlinePlayerManager;
import dev.authorises.CentralC.model.proxy.ProxyManager;
import dev.authorises.CentralC.model.punishments.Punishment;
import dev.authorises.CentralC.model.punishments.PunishmentsCache;
import dev.authorises.CentralC.model.server.ServerManager;
import dev.authorises.CentralC.mongo.MongoManager;
import dev.authorises.CentralC.sockets.RegisteredSocketManager;
import dev.authorises.CentralC.sockets.SocketManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;

@SpringBootApplication
public class CentralCApplication {

	public static AccessManager accessManager;
	public static ServerManager serverManager;
	public static ProxyManager proxyManager;
	public static MongoManager mongoManager;
	public static PunishmentsCache punishmentsCache;
	public static OnlinePlayerManager onlinePlayerManager;
	public static RegisteredSocketManager registeredSocketManager;
	public static SocketManager socketManager;
	public static MapLeaderboardManager mapLeaderboardManager;
	public static Gson gson = new Gson();

	public static void main(String[] args) throws UnknownHostException {

		// Manage api access tokens
		accessManager = new AccessManager();
		accessManager.addToken(new AccessToken(PermissionType.INTERNAL, "3bed2cb3a3acf7b6a8ef408420cc682d5520e26976d354254f528c965612054f"));
		accessManager.addToken(new AccessToken(PermissionType.GLOBAL, "8001c27439650c5c5a6b4ed94163b5ddeb4476362c71380e613fa20dfffcef50"));

		serverManager = new ServerManager();

		proxyManager = new ProxyManager();

		String mongoKey = System.getenv("MONGODB_URI");
		if(mongoKey==null){
			System.out.println("Mongo key not found");
			System.exit(0);
		}

		mongoManager = new MongoManager(mongoKey);

		punishmentsCache = new PunishmentsCache();

		registeredSocketManager = new RegisteredSocketManager();

		onlinePlayerManager = new OnlinePlayerManager();

		mapLeaderboardManager = new MapLeaderboardManager();

		// Start the spring boot application
		SpringApplication.run(CentralCApplication.class, args);

		// Start the Socket.IO Server
		socketManager = new SocketManager();
	}

}
