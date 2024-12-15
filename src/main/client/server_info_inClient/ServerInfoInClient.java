package main.client.server_info_inClient;

public class ServerInfoInClient {

    private final String server_address = "127.0.0.1";
    private final int server_port = 4242;
    private int userId = 0;
    private String userNameOfCurrent;
    public int getServer_port() {
        return server_port;
    }
    public String getServer_address() {
        return server_address;
    }
    public void setUserId(int user_id){
        this.userId = user_id;
    }
    public int getUserId(){
        return userId;
    }
    public void setUserNameOfCurrent(String user){
        this.userNameOfCurrent = user;
    }
    public String getUserNameOfCurrent(){
        return userNameOfCurrent;
    }
}
