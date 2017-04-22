import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptTest {
    public static void main(String ... args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();

        for (ScriptEngineFactory f : manager.getEngineFactories()) {
            System.out.println(f.getEngineName() + " -> " + f.getLanguageName() + " (" + String.join(", ", f.getExtensions()) + ")");
        }

        System.out.println("");

        ScriptEngine python_engine;
        ScriptEngine javascript_engine;
        ScriptEngine lua_engine;

        long start_time;
        long end_time;

        System.out.println("Testing Python interpreter...");

        python_engine = manager.getEngineByExtension("py");
        start_time = System.currentTimeMillis();

        python_engine.eval("print(\"Hello, world!\")");

        end_time = System.currentTimeMillis();
        System.out.println("Time taken: " + String.valueOf((end_time - start_time)) + "ms\n");

        System.out.println("Testing JavaScript interpreter...");

        javascript_engine = manager.getEngineByExtension("js");
        start_time = System.currentTimeMillis();

        javascript_engine.eval("print(\"Hello, world!\")");

        end_time = System.currentTimeMillis();
        System.out.println("Time taken: " + String.valueOf((end_time - start_time)) + "ms\n");

        System.out.println("Testing Lua interpreter...");

        lua_engine = manager.getEngineByExtension("lua");
        start_time = System.currentTimeMillis();

        lua_engine.eval("print(\"Hello, world!\")");

        end_time = System.currentTimeMillis();
        System.out.println("Time taken: " + String.valueOf((end_time - start_time)) + "ms\n");
    }
}
