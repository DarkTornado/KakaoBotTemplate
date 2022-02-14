package javax.lang.model;

//임시 방편
//https://github.com/mozilla/rhino/issues/1149

public class SourceVersion {
    public static SourceVersion latestSupported() {
        return new SourceVersion();
    }
    public final int ordinal() {
        return 8;
    }
}