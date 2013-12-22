package com.sishuok.spring4.script;

import org.junit.Test;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-19
 * <p>Version: 1.0
 */
public class ScriptTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        ScriptEvaluator scriptEvaluator = new GroovyScriptEvaluator();

        //ResourceScriptSource 外部的
        ScriptSource source = new StaticScriptSource("i+j");
        Map<String, Object> args = new HashMap<>();
        args.put("i", 1);
        args.put("j", 2);
        System.out.println(scriptEvaluator.evaluate(source, args));
    }
}