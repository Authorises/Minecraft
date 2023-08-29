package dev.authorises.CentralC.shutdown;

import dev.authorises.CentralC.CentralCApplication;

import javax.annotation.PreDestroy;

public class TerminateBean {

    @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed!");
        CentralCApplication.socketManager.socketServer.stop();
    }
}
