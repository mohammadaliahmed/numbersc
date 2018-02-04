
public class UserDetails {
	String username,email,password,active,phone,macAdress,time,code;

	
	
	
	public UserDetails() {
		super();
	}


	public UserDetails(String username, String email, String password,  String phone,String active, String macAdress,
			String time,String code) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.active = active;
		this.phone = phone;
		this.macAdress = macAdress;
		this.time = time;
		this.code=code;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getActive() {
		return active;
	}


	public void setActive(String active) {
		this.active = active;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMacAdress() {
		return macAdress;
	}

	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
