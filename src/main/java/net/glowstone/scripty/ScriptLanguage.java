package net.glowstone.scripty;

import javax.script.ScriptEngine;

public enum ScriptLanguage {
    JAVASCRIPT("JavaScript", "print(\"Hello, world!\")", "js"), PYTHON("Python", "print(\"Hello, world!\")", "py"), LUA("LUA", "print(\"Hello, world!\")", "lua");

    private final String name, sample;
    private final ScriptEngine engine;

    ScriptLanguage(String name, String sample, String extension) {
        this.name = name;
        this.sample = sample;
        this.engine = ScriptyMod.ENGINE_MANAGER.getEngineByExtension(extension);
        System.out.println("TEST> Engine exists for " + name + " -> " + (engine != null));
    }


    public String getName() {
        return name;
    }

    public String getSample() {
        return sample;
    }

    public ScriptEngine getEngine() {
        return engine;
    }
}
