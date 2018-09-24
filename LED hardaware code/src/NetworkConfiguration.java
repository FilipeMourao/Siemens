
public class NetworkConfiguration {
	String Name;
	String Password;
	
	public NetworkConfiguration(String name, String password) {
		super();
		Name = name;
		Password = password;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}

}
