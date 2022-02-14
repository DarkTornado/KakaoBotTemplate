package com.darktornado.kakaobottemplate;

import android.widget.Toast;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.ScriptableObject;

public class RhinoAdapter {

    final private android.content.Context ctx;
    public ScriptableObject scope;

    public RhinoAdapter(android.content.Context ctx) {
        this.ctx = ctx;
    }

    public boolean reload(String source) {
        try {
            Context rhino = Context.enter(); //우리가 아는 그 Context 아님
            rhino.setOptimizationLevel(-1);
            rhino.setLanguageVersion(Context.VERSION_ES6);
            //scope = rhino.initSafeStandardObjects();
            scope = new ImporterTopLevel(rhino); //이걸로 해야 importClass랑 importPackage 사용 가능
            rhino.evaluateString(scope, source, "JavaScript", 1, null);
            Context.exit();
            return true;
        } catch (Exception e) {
            Context.exit();
            Toast.makeText(ctx, "스크립트 리로드 실패\n" + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean callEventListener(String name, Object[] args) {
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);
            rhino.setLanguageVersion(Context.VERSION_ES6);
            Function func = (Function) scope.get(name, scope);
            func.call(rhino, scope, scope, args);
            Context.exit();
            return true;
        } catch (ClassCastException e) { //JS쪽에서 없는 함수 호출하면 뜨는 오류
            Context.exit(); //아무것도 안하고 그냥 무시
            return false;
        } catch (Exception e) {
            Context.exit();
            Toast.makeText(ctx, "이벤트 리스너(" + name + ") 호출 실패\n" + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
