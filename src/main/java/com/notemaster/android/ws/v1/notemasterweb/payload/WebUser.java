package com.notemaster.android.ws.v1.notemasterweb.payload;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.notemaster.android.ws.v1.notemasterweb.database.constants.UserTableConstants;

public class WebUser implements UserTableConstants {

    private String name;
    private String password;
    private String device_id;
    private String remark;
    private String roles;
    private String user_id;
    

	public WebUser() {
		super();
	}

	public WebUser(ResultSet rs) {
		super();
		try {
			rs.beforeFirst(); // reset pointer to before the first record or I will back at the end with Next()
			while (rs.next()) {
				this.device_id = rs.getString(USR_DID);
				this.name = rs.getString(USR_NAME);
				this.password = rs.getString(USR_PASSWRD);
				this.remark = rs.getString(USR_REMARK);
				this.user_id = rs.getString(USR_GUID);
				this.roles = rs.getString(USR_ROLES);
				break; // only the first record is used
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public WebUser(String name, String password, String device_id, String remark, String user_role, String user_id) {
		super();
		this.name = name;
		this.password = password;
		this.device_id = device_id;
		this.remark = remark;
		this.roles = user_role;
		this.user_id = user_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((device_id == null) ? 0 : device_id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebUser other = (WebUser) obj;
		if (device_id == null) {
			if (other.device_id != null)
				return false;
		} else if (!device_id.equals(other.device_id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "WebUser [name=" + name + ", password=" + password + ", device_id=" + device_id + ", remark=" + remark
				+ ", roles=" + roles + ", user_id=" + user_id + "]";
	}
	
	
}
