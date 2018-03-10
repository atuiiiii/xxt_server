package bid.xiaocha.xxt_server.model;

public class LoginState {
	
	private State state;
	
	private String jwt;
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getRongCloudToken() {
		return rongCloudToken;
	}

	public void setRongCloudToken(String rongCloudToken) {
		this.rongCloudToken = rongCloudToken;
	}

	private String rongCloudToken;
	
	public enum State{
		success,
		checkoutError,
		serverError
	}
}
