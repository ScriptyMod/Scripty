package net.glowstone.scripty;

public enum ScriptLanguage {
    JAVASCRIPT("JavaScript"), PYTHON("Python"), LUA("LUA");

    private final String name;

    ScriptLanguage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
