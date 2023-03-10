package com.edgar.bookrental.models.user;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
	USER("user"), ADMIN("admin");

	String roleText;

	Role(String roleText) {
		this.roleText = roleText;
	}

	@JsonCreator
	public String getRoleText() {
		return this.roleText;
	}

	

private static HashMap<String, Role> roles;


static {
    roles = new HashMap<>();
    for (Role r : Role.values()) {
        roles.put(r.toString(), r);
    }
}


@JsonCreator
public static Role parse(String s) {
    return roles.get(s);
}



}


