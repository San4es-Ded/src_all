package rockstar.client.network;


import rockstar.client.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import javax.annotation.Nonnull;
import rockstar.client.network.MediaTypes;
import rockstar.client.network.RequestBody;
import rockstar.client.network.MediaType;

public class FileRequestBody
extends RequestBody {
    private final File internalField0148;

    public FileRequestBody(File file) {
        this(MediaTypes.internalField1097, file);
    }

    public FileRequestBody(MediaType typedValue038, File file) {
        super(typedValue038);
        this.internalField0148 = file;
    }

    @Override
    public boolean internalMethod02204() {
        return true;
    }

    @Override
    public int internalMethod02203() {
        return (int)this.internalField0148.length();
    }

    @Override
    @Nonnull
    protected InputStream internalMethod00691() throws IOException {
        return Files.newInputStream(this.internalField0148.toPath(), new OpenOption[0]);
    }
}

