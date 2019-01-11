package mysql;

public class User{
	private String userid; private String nickname; private byte[] portrait;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPortrait() {
		return new String(portrait);
	}
	public void setPortrait(byte[] portrait) {
		this.portrait = portrait;
	}
}
