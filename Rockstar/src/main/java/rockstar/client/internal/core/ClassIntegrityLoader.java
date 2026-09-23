package rockstar.client.internal.core;



import rockstar.client.internal.network.*;
import rockstar.client.*;
import rockstar.client.internal.network.ScriptSecurityGuard;

public final class ClassIntegrityLoader
extends ClassLoader {
    public ClassIntegrityLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public Class<?> loadClass(String string, boolean bl) throws ClassNotFoundException {
        try {
            ScriptSecurityGuard.internalMethod03423(string);
        }
        catch (ScriptSecurityGuard.InternalType0290 nestedValue0109) {
            throw new ClassNotFoundException(nestedValue0109.getMessage(), nestedValue0109);
        }
        return super.loadClass(string, bl);
    }
}

