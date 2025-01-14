package fr.pick_eat.auth.scope;

public enum EScope {

    USER,
    GUEST;

    public String getAuthority() {
        return this.name();
    }

    public String getScope() {
        return this.name();
    }
}

