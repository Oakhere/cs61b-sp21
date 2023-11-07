package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;

/** Represent a blob object, i.e. the saved contents of files. */
public class Blob implements Serializable {
    /** The file that the blob represents. */
    File f;
    String contents;
    public Blob(File f) {
        this.f = f;
        this.contents = readContentsAsString(f);
    }
    /** Save the blob as a file in the disk with the file name being its SHA-1 code. */
    public void saveBlob() throws IOException {
        File f = join(Repository.GITLET_DIR, sha1(serialize(this)) + ".txt");
        f.createNewFile();
        writeObject(f, this);
    }
}
